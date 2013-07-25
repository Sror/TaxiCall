//
//  MyHttp_remoteClient.h
//  qihuaTaxi ios client
//
//  Created by Alex on 9/28/11.
//  Copyright (c) 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DataSingleton.h"
#import "JSONKit.h"
#import "constant.h"
#import "HttpConnection.h"
#import "HttpClientDelegate.h"
#import "AsynHttpConnection.h"


@interface HttpClient : HttpConnection <AsynHttpConnectionDelegate>
{
    NSString* pageSource;

    
    id<HttpClientDelegate>  _delegate;

}

@property (nonatomic,retain) id<HttpClientDelegate> delegate;

- (id)initWithDelegate:(id<HttpClientDelegate>) delegate;


-(void)sendTaxiOrderRequest:(NSString*)latstring withLonstring:(NSString*)lonstring;
-(void)getOrderInformation:(NSString*)order_idString;
-(void)getPolicyState;
@end


