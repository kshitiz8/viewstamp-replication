viewstamp-replication
=====================
Implementation based on 'Viewstamped Replication Revisited' by Liskov and Cowling http://pmg.csail.mit.edu/papers/vr-revisited.pdf


Code Description
-----------------

* vr.code.service -  this package contains all the VR code services including all the RPCs
* vr.code.processes - consist of asynchronous processes like polling the request queue, checking for primary's timeout, etc.
* vr.code.client - consist of the client API invoked by the service code or processes on VR code
* vr.proxy.client - consists of the vr proxy implemetation

RPC are implemented in Thrift

Features Currenlty Supported:

1. Normal operation : server accepts concurrent request, buffers in a queue. Replicate it on multiple replica before commiting. Service remains up if f+1 replicas are up.
2. View Change: If primary goes down, other replicas start view change protocol.
3. state recovery: A new replica joining updates its state to the current log.

Todo:
Efficient View Change and recovery
Dynamic Reconfiguratation 

Setup in Eclipse
-----------------
1. Install Thrift:
https://thrift.apache.org/docs/install/

2. Checkout code from this repo
3. Import the maven project in eclipse
4. Update Java compiler in preference to be 1.6 or above
5. Configure server.properties to specify qourom size.
6. Execute server vr.code.service.VRCodeServer with server id as argument (server id = 0,1,2 for 3 server setup )
7. execute Application Client application.client.AppClient and execute request. The application service is a dummy method responding back with the given input.




