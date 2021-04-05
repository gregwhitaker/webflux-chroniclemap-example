# webflux-chroniclemap-example
An example of retrieving data from an off-heap [Chronicle Map](https://github.com/OpenHFT/Chronicle-Map) in a [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html) application.

The example application consists of a webservice that serves product data from a Redis
database. When product data is retrieved it is cached using Chronicle Map and returned from cache
on subsequent requests.

## Building the Example
Run the following command to build the example application:

    ./gradlew clean build

## Running the Example
Follow the steps below to run the example:

1. Run the following command to start the application and a Redis database:

        ./gradlew bootRunLocal

2. Run the following command to retrieve a product:

       curl "http://localhost:8080/products/10"
   
   If successful, you will see the following response:

       {"id":"10","name":"Product10","description":"Product10 Description","active":false,"start_date":"2021-01-01T00:00Z","end_date":"2021-01-31T00:00Z"}

## Bugs and Feedback
For bugs, questions, and discussions please use the [Github Issues](https://github.com/gregwhitaker/webflux-chroniclemap-example/issues).

## License
MIT License

Copyright (c) 2021 Greg Whitaker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.