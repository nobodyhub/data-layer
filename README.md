# Data Layer

A database service invoked through gRPC 

## Features

1. Use [Hibernate ORM](http://hibernate.org/orm/) to support vairous DB type via same interface
1. Use [Avro](http://avro.apache.org/) to encode/decode the table Entity
1. Use [gRPC](https://grpc.io/) + [Protocol buffers](https://developers.google.com/protocol-buffers/) to manage the data transfer
1. Use [gRPC streaming](https://grpc.io/docs/guides/concepts.html#server-streaming-rpc) to manage the DB transactions
1. Preload schema for necessary classes
1. Provide HTTP interface for health check


## Why use Avro to encode/decode
Compared to other de/serialize tool, like Jackson, Thrift, Protocal Buffers, Avro has advantange in following aspects for handling entity: 
1. Dynamic schema: give a high-level freedom to manage the schema programatically
1. Human-readable schema in JSON: easy for error checking
1. No need to declare IDs: ID fields does not have much benefits in de/serialize but introduce cost in versionning.

## Hints
1. The entity should be defined according to the business requirement.

## Performance
> Measured by: [JMH - Java Microbenchmark Harness](http://openjdk.java.net/projects/code-tools/jmh/)

### DB Access: gRPC vs. Native ORM(in-memory H2)
> - @see DataLayerServerTest
> - @see DataLayerClientTest

Although the performance of gRPC will be absolutely slower than the Native ORM(which is already lower than than using SQL, LOL),
the result is still worse than my expectation, which is `≈4 ops/ms`(Native ORM) vs. `≈0.5 ops/ms`(gRPC).

The acceptable result of performance ratio is 1:2 to 1:3 regardless of the network issues.

> - TODO: try to improve the message serialization
>    - @see: [Encode/Decode: Avro vs. ProtoBuf](#encodedecode-avro-vs-protobuf)

### Encode/Decode: Avro vs. ProtoBuf
> - @see AvroSchemaConverterTest
> - @see ProtoDataTest

Both Avro and ProtoBuf support dynamically generating schema/protocol.
By using this generated schema/protocol, any user-defined class would be have a corresponding type/message in
Avro/ProtoBuf.
The performance of encode/decode the instance using reflection is very close, which is `≈195 ops/ms` for AvroData and `≈475 ops/ms` ProtoData.
The main cost is to use reflection to get/set the values of instance fields.

With a protocol definition, the protobuf would reach a performance of `≈4200 ops/ms` because the assignmen is straightforward. 

> TODO: write maven plugin to generate .proto, which will be converted to message classes by `protobuf-maven-plugin`.


 

 