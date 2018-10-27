package io.edkek.ethereum.jevm.abi;

import io.edkek.ethereum.jevm.Contract;
import io.edkek.ethereum.jevm.abi.functions.ABIEntry;
import io.edkek.ethereum.jevm.abi.functions.ABIFunctionBuilder;
import io.edkek.ethereum.jevm.abi.parameters.ABIEventParameter;
import io.edkek.ethereum.jevm.abi.parameters.ABIParameter;
import io.edkek.ethereum.jevm.annotations.*;
import io.edkek.ethereum.jevm.types.Address;
import io.edkek.ethereum.jevm.types.TransactionOptions;
import org.apache.commons.codec.DecoderException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ABIEncoder {
    private static boolean parm_warned = false;

    public static ABIEncoded encode(Contract contract) {
        return encode(contract.getClass());
    }

    public static ABIEncoded encode(Class<? extends Contract> _class) {
        List<Method> methods = Arrays.asList(_class.getMethods());

        Class parent = _class.getSuperclass();
        while (parent != null) {
            methods.addAll(Arrays.asList(parent.getDeclaredMethods()));
            parent = _class.getSuperclass();
        }

        Map<Method, ABIEntry> functions = new HashMap<>();
        for (Method m : methods) {

            //Start a new builder for a new function
            ABIFunctionBuilder builder = ABIFunctionBuilder.create();

            //Default to method name in reflection
            String name = m.getName();

            //Now see what kind of function this is
            //And check the stateMutability
            Annotation[] prop = m.getDeclaredAnnotations();
            String[] parameterNames = null;

            boolean anonymous = false; //Used for events
            boolean isEvent = false;

            //Always default to the function type, in case we don't find an annotation saying otherwise
            builder.setType("function");

            //Always default to a nonpayable state, in case we don't find an annotation saying otherwise
            builder.setStateMutability("nonpayable");

            for (Annotation annotation : prop) {
                if (annotation instanceof Event) {
                    builder.setType("event");

                    Event e = (Event)annotation;

                    anonymous = e.isAnonymous();
                    isEvent = true;
                }

                if (annotation instanceof View) {
                    builder.setStateMutability("view");

                    View v = (View)annotation;
                    parameterNames = v.parameterNames();
                    if (!v.name().equals(""))
                        name = v.name();
                }

                if (annotation instanceof Pure) {
                    builder.setStateMutability("pure");

                    Pure v = (Pure)annotation;
                    parameterNames = v.parameterNames();
                    if (!v.name().equals(""))
                        name = v.name();
                }

                if (annotation instanceof Payable) {
                    builder.setStateMutability("payable");

                    Payable v = (Payable)annotation;
                    parameterNames = v.parameterNames();
                    if (!v.name().equals(""))
                        name = v.name();
                }

                if (annotation instanceof NonPayable) {
                    builder.setStateMutability("nonpayable");

                    NonPayable v = (NonPayable)annotation;
                    parameterNames = v.parameterNames();
                    if (!v.name().equals(""))
                        name = v.name();
                }
            }


            //Set the function name
            builder.setName(name);

            //Now generate the inputs
            ABIParameter[] parameters = generateParameters(m, isEvent, parameterNames);

            builder.setInputs(parameters);

            if (!isEvent) {
                ABIParameter[] outputs = generateOutputs(m);

                if (outputs.length > 0) {
                    builder.setOutputs(outputs);
                }
            }

            if (!isEvent) {
                functions.put(m, builder.build());
            } else {
                functions.put(m, builder.buildEvent(anonymous));
            }
        }

        return new ABIEncoded(functions);
    }

    private static ABIParameter[] generateOutputs(Method method) {
        if (method.getReturnType().equals(Void.TYPE)) {
            return new ABIParameter[0];
        } else {
            //TODO Support multiple outputs

            String name = "";
            String type = typeFromClass(method.getReturnType());

            return new ABIParameter[] {
                new ABIParameter(name, type)
            };
        }
    }

    private static int getTrueParameterCount(Method method) {
        int i = 0;
        Parameter[] parm = method.getParameters();
        for (Parameter p : parm) {
            if (!p.getType().equals(TransactionOptions.class))
                i++;
        }

        return i;
    }

    private static ABIParameter[] generateParameters(Method method, boolean isEvent, String[] parameterNames) {
        ABIParameter[] abiParameters = new ABIParameter[getTrueParameterCount(method)];
        Parameter[] javaParameters = method.getParameters();

        for (int i = 0; i < javaParameters.length; i++) {
            Parameter parm = javaParameters[i];

            if (parm.getType().equals(TransactionOptions.class))
                continue; //Ignore this parameter as its metadata

            String name;
            if (parameterNames != null && i < parameterNames.length) {
                name = parameterNames[i];
            } else if (parm.isNamePresent()) {
                name = parm.getName();
            } else {
                if (!parm_warned) {
                    System.err.println("Parameter names are not present! Compile with the -parameters argument, falling back to @Name annotation");
                    parm_warned = true;
                }

                Name nameAnnon = parm.getAnnotation(Name.class);
                if (nameAnnon == null)
                    throw new IllegalArgumentException("Parameter names are not present and no @Name annotation found! Try compiling with the -parameters argument or add a @Name annotation to the parameter!");

                name = nameAnnon.name();
            }

            String type = typeFromClass(parm.getType());

            if (isEvent) {
                boolean isIndexed = parm.getAnnotation(Indexed.class) != null;

                abiParameters[i] = new ABIEventParameter(name, type, isIndexed);
            } else {
                abiParameters[i] = new ABIParameter(name, type);
            }
        }

        return abiParameters;
    }

    private static String typeFromClass(Class<?> type) {
        if (type.isPrimitive()) {
            //If it's one of the primitive types, we can infer it's type using the bit count

            if (type.equals(byte.class)) {
                return "int8";
            } else if (type.equals(short.class)) {
                return "int16";
            } else if (type.equals(int.class)) {
                return "int32";
            } else if (type.equals(long.class)) {
                return "int64";
            } else if (type.equals(boolean.class)) {
                return "bool";
            } else {
                throw new IllegalArgumentException("Invalid type " + type.getName());
            }
        } else {
            //If it's not a primitive, see if it's a known type.

            if (type.equals(Address.class)) {
                return "address";
            } else if (type.equals(BigInteger.class)) {
                return "uint256";
            } else {
                //TODO Add cases for the remaining types not covered
                //TODO Add support for custom structs/classes

                throw new IllegalArgumentException("Invalid type " + type.getName());
            }
        }
    }

    public static Object decodeResult(Class<?> type, String rawResult) {
        if (type.isPrimitive()) {
            //If it's one of the primitive types, we can infer it's type using the bit count

            if (type.equals(byte.class)) {
                return Byte.parseByte(rawResult, 16);
            } else if (type.equals(short.class)) {
                return Short.parseShort(rawResult, 16);
            } else if (type.equals(int.class)) {
                return Integer.parseInt(rawResult, 16);
            } else if (type.equals(long.class)) {
                return Long.parseLong(rawResult, 16);
            } else if (type.equals(boolean.class)) {
                return Integer.parseInt(rawResult, 16) > 0;
            } else {
                throw new IllegalArgumentException("Invalid type " + type.getName());
            }
        } else {
            //If it's not a primitive, see if it's a known type.

            if (type.equals(Address.class)) {
                rawResult = rawResult.substring(2);

                if (rawResult.length() == 64)
                    rawResult = rawResult.substring(24);

                try {
                    return new Address(rawResult);
                } catch (DecoderException e) {
                    throw new IllegalArgumentException("Invalid address response " + rawResult);
                }
            } else if (type.equals(BigInteger.class)) {
                byte radix = 10;
                if(rawResult.startsWith("0x")) {
                    rawResult = rawResult.substring(2);
                    radix = 16;
                } else if(rawResult.contains("a") || rawResult.contains("b") ||
                        rawResult.contains("c") || rawResult.contains("d") ||
                        rawResult.contains("e") || rawResult.contains("f")) {
                    radix = 16;
                }

                return new BigInteger(rawResult, radix);
            } else {
                //TODO Add cases for the remaining types not covered
                //TODO Add support for custom structs/classes

                throw new IllegalArgumentException("Invalid type " + type.getName());
            }
        }
    }
}
