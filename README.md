# RestRequestJsonComparator

This project is a simplified framework for processing 1000s of REST requests and comparing JSON responses from one request to another in a multi-threaded fashion.

It is intended to run in a multi-threaded mode to accommodate high input flow. Keeping in mind that this solution may be run on a variety of systems with varying configurations, I made thread-count and load per thread user-configurable.

More enhancements/optimization will be done as I progress.

## Features:

- Computes the load for each thread based on number of endpoints in file, thread count set in config.json and loadperthread set in config.json
- Spawns threads based on above computation and assigns load to each thread
- Reads endpoints from two different input files and sends http requests to the endpoints.
- Compare the responses of endpoints from both the input files line by line
- The output is written into an result.csv file by default, can be changed in config.json

