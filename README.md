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
