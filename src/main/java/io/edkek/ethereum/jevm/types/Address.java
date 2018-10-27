package io.edkek.ethereum.jevm.types;


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.util.Arrays;

public class Address {
    private byte[] address = new byte[20];

    public Address(byte[] address) {
        this.address = address;
    }

    public Address(String hex) throws DecoderException {
        if (hex.startsWith("0x"))
            hex = hex.substring(2);

        address = Hex.decodeHex(hex.toCharArray());
    }

    public String addressString() {
        return toString();
    }

    public String toString() {
        return "0x" + Hex.encodeHexString(address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address1 = (Address) o;

        return Arrays.equals(address, address1.address);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(address);
    }
}
