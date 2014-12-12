viewstamp-replication
=====================

Systems:
Application Client
VR Proxy
VR Service
Application Service

Only VR Proxy and VR code is implemented here. 
Appliaction Client integrates VR Proxy APIs and call the execute method to invoke any operation.
The VR proxy communicates with the VRCode to execute the operation on replicas running application service below VR Service.

Stages:

1. Normal Operation
  Normal operation consists of following RPCs:
  * RpcRequest
  * RpcPrepare
  * RpcCommit
  * GetState
  
2. View Change 
  * StartViewChange
  * DoViewChange
  * StartView
  
3. Recovery Phase
  * Recovery
