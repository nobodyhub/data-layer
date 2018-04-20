# Data Layer

A database service invoked through gRPC 

## Features

1. use [Hibernate ORM](http://hibernate.org/orm/) to support vairous DB type via same interface
1. use [Avro](http://avro.apache.org/) to encode/decode the table Entity
1. use [gRPC](https://grpc.io/) + [Protocol buffers](https://developers.google.com/protocol-buffers/) to manage the data transfer
1. use [gRPC streaming](https://grpc.io/docs/guides/concepts.html#server-streaming-rpc) to manage the DB transactions
