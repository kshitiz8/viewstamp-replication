namespace java vr.replica.server.thrift
typedef i64 long
typedef i32 int

enum ResponseCode {
	redirected,
    accepted,
    comleted,
    failed 
}  

struct RedirectResponse{
	1: int primaryReplica; 
}

struct SuccessResponse{
	1: long requestNumber;
	2: string responseString;
}
struct FailureResponse{
	1: long requestNumber;
	2: string responseString;
}

union ResponseUnion{
	1: RedirectResponse redirectResponse;
	2: string acceptedResponse;
	3: SuccessResponse successResponse;
	4: FailureResponse failureResponse;
}

struct ResponseStruct {
    1: required ResponseCode responseCode;
    2: int viewNumber;
    3: ResponseUnion responseUnion;   
    
}

service ReplicaService{
        ResponseStruct request(1:string op, 2: string c, 3: long r),
}
