//
//  MyHttp_remoteClient.m
//  qihuaTaxi ios client
//
//  Created by Alex on 9/28/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import "HttpClient.h"
#import "DataSingleton.h"

@implementation HttpClient

@synthesize delegate=_delegate;


-(void) dealloc{
    self.delegate=nil;
    [super dealloc];

}

- (id)init
{
    self = [super init];
    if (self) {
        
    }
    return self;
}
- (id)initWithDelegate:(id) delegate
{
    self = [super init];
    if (self) {
        self.delegate=delegate;
    }
    return self;
}

- (Boolean) checkinput:(NSString*) userSearchInput
{
    if (userSearchInput.length > 1) {
        return TRUE;
    }
    return FALSE;
    
}
- (BOOL) testReachAblity
{
    BOOL result = YES;
    //result = [Reachability testReachAblity];
    
    return result;
}


-(void)sendTaxiOrderRequest:(NSString*)latstring withLonstring:(NSString*)lonstring
{
   
    AsynHttpConnection *connection = [[AsynHttpConnection alloc] initWithDelegate:self];
    [connection postAsynchronousMethodUrl:@"http://124.207.107.80/taxi/find_by_loc.php?" params:[NSMutableDictionary dictionaryWithObjectsAndKeys:@"xxxx", @"Auth",
                                                      latstring, @"lat",
                                                      lonstring, @"lng",
                                                      nil]];
    [connection release];


   
}
-(void)getOrderInformation:(NSString*)order_idString
{
    AsynHttpConnection *connection = [[AsynHttpConnection alloc] initWithDelegate:self];
    [connection getAsynchronousMethodUrl:@"http://124.207.107.80/taxi/result.php?" params:[NSMutableDictionary dictionaryWithObjectsAndKeys: @"xxxx", @"Auth",
                                                    order_idString, @"order_id",
                                                        nil]];
    [connection release];
}

-(void)getPolicyState
{
    AsynHttpConnection *connection = [[AsynHttpConnection alloc] initWithDelegate:self];
    [connection getAsynchronousMethodUrl:@"http://219.237.242.71:8091/result.php?" params:[NSMutableDictionary dictionaryWithObjectsAndKeys: @"xxxx", @"Auth",                                                                                           nil]];
    [connection release];
}

//------------------------Login, Lostpasswd, Register


#pragma mark - AsynHttpConnectionDelegate Method
- (void)didReceiveStop:(AsynHttpConnection *)controller {
    NSString *responseData = [[[NSString alloc] initWithData:controller.m_data encoding:NSUTF8StringEncoding] autorelease];
    if(responseData!=nil) {
        NSLog(@"response:%@",responseData);
        NSDictionary *resultsDictionary = (NSDictionary *)[responseData objectFromJSONString];
        if (1) {
            ////always return to success state, 
            if ([self.delegate respondsToSelector:@selector(doSuccess:)]) {
                [self.delegate performSelector:@selector(doSuccess:) withObject:resultsDictionary];
            }
        }
        else {
            if ([self.delegate respondsToSelector:@selector(doFail:)]) {
                [self.delegate performSelector:@selector(doFail:) withObject:[resultsDictionary valueForKey:@"code"]];
            }
        }
    }
    else{
        if ([self.delegate respondsToSelector:@selector(doNetWorkFail)]) {
            [self.delegate performSelector:@selector(doNetWorkFail)];
        }
    }
}

- (void)didReceiveStart:(AsynHttpConnection *)controller
{
    NSLog(@"in async HttpConnect Did receive start");
}
- (void)didReceiveResponse:(AsynHttpConnection *)controller httpResponse:(NSHTTPURLResponse *) httpResponse
{
    NSLog(@"in async HttpConnect Did didReceiveResponse start");
}
- (void)didReceiveData:(AsynHttpConnection *)controller data:(NSData *)data
{
     NSLog(@"in async HttpConnect Did didReceiveData start");
}

@end
