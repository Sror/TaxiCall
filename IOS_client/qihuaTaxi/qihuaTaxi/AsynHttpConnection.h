//
//  AsynHttpConnection.h
//  qihuaTaxi
//
//  Created by 际航 杨 on 7/18/12.
//  Copyright (c) 2012 index informatics. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol AsynHttpConnectionDelegate;
@interface AsynHttpConnection : NSObject <NSURLConnectionDataDelegate>
{
//    for Asynchronize Post
//    start
    NSURLConnection *connection;                //√
	NSString *statusText;                       //√
    id <AsynHttpConnectionDelegate> delegate;
    NSNumber *contentLength;                    //√
    NSString *contentType;                      //√
    NSMutableData *m_data;
    BOOL finished;
//    end
}
@property (nonatomic, retain) NSURLConnection *connection;
@property (nonatomic, copy) NSString *statusText;
@property (nonatomic, assign) id <AsynHttpConnectionDelegate> delegate;
@property (nonatomic, retain) NSNumber *contentLength;
@property (nonatomic, retain) NSString *contentType;
@property (nonatomic, retain) NSMutableData *m_data;
@property (nonatomic, assign) BOOL finished;

- (id)initWithDelegate:(id)myDelegate;
- (void) getAsynchronousMethodUrl:(NSString *)relativeUrl  params:(NSDictionary *)params;
- (void) postAsynchronousMethodUrl:(NSString *)relativeUrl  params:(NSDictionary *)params;
- (void) postAsynchronousMethodUrlForTestServer:(NSString *)relativeUrl  params:(NSDictionary *)params;

- (void) postAsynchronousMethodData:(NSString *)relativeUrl  data:(NSData *)data withtDictionary:(NSDictionary *)dic;
- (void) postAsynchronousMethodUrlForTestImageServer:(NSString *)relativeUrl  params:(NSDictionary *)params;

@end

@protocol AsynHttpConnectionDelegate <NSObject>
- (void)didReceiveStop:(AsynHttpConnection *)controller;
- (void)didReceiveStart:(AsynHttpConnection *)controller;
- (void)didReceiveResponse:(AsynHttpConnection *)controller httpResponse:(NSHTTPURLResponse *) httpResponse;
- (void)didReceiveData:(AsynHttpConnection *)controller data:(NSData *)data;
@end
