# jevm
A Java API to interact with Smart Contracts

# TODO
Documentation

# Usage

```java
//Make web3 object with some provider
Web3 web3 = new Web3(new HttpProvider("https://mainnet.infura.io/v3/1fdd2db1469a4aae8909b8e4075ddc5c"));

//Create interface class from web3 object
ERC721Token token = web3.getContract(ERC721Token.class, "0x5CAeBd3b32e210E85CE3E9d51638B9C445481567");

//Invoke functions in contract
System.out.println("Owner of contract is: " + token.owner());
System.out.println("Balance of owner is: " + token.balanceOf(token.owner()));
System.out.println("Owner of token 10810196105056141439 is " + token.ownerOf(new BigInteger("10810196105056141439")));
```


# Example Contract Interface

```java
public interface ERC721Token extends Ownable {

    @View
    BigInteger balanceOf(@Name(name = "owner") Address owner);

    @View
    Address ownerOf(@Name(name = "tokenId") BigInteger tokenId);
}

public interface Ownable extends Contract {

    @Event
    void OwnershipRenounced(
           @Name(name = "previousOwner") @Indexed Address previousOwner
    );

    @Event
    void OwnershipTransferred(
            @Name(name = "previousOwner") @Indexed Address previousOwner,
            @Name(name = "newOwner") @Indexed Address newOwner
    );


    @View
    Address owner();

    @NonPayable
    void renounceOwnership(TransactionOptions options);

    //Test whether no annotation marks function as nonpayable
    void transferOwnership(
            @Name(name = "newOwner") Address newOwner,
            TransactionOptions options
    );
}
```

