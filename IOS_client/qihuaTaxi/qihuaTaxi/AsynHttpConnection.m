//
//  AsynHttpConnection.m
//  qihuaTaxi
//
//  Created by 际航 杨 on 7/18/12.
//  Copyright (c) 2012 index informatics. All rights reserved.
//

#import "AsynHttpConnection.h"

#import "HttpConnection.h"

@implementation AsynHttpConnection
@synthesize connection, statusText, delegate, contentLength, contentType, m_data, finished;

- (NSMutableData *)m_data {
    if (m_data == nil) {
        m_data = [[NSMutableData alloc] init];
    }
    return m_data;
}

- (id)initWithDelegate:(id)myDelegate {
    self = [super init];
    if (self) {
        self.delegate = myDelegate;
    }
    return self;
}

- (NSString*)paramsFromDictionary:(NSDictionary*)params {
    if (params) {
        NSMutableString *str = [NSMutableString stringWithCapacity:0];
        if (params) {
            [params enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
                [str appendFormat:@"%@=%@&",key,obj];
            }];
        }
        return str;
    }
    return nil;
}
-(NSString *) getURL:(NSString *)url queryParameters:(NSDictionary *)params{
    
    NSString* fullPath =[NSString stringWithFormat:@"%@%@",BASE_URL,url];
	if (params) {
        // Append base if specified.
        NSMutableString *str = [NSMutableString stringWithCapacity:0];
        if (fullPath) {
            [str appendString:fullPath];
        }
        
        // Append each name-value pair.
        if (params) {
            int i;
            NSArray *names = [params allKeys];
            for (i = 0; i < [names count]; i++) {
                if (i == 0) {
                    [str appendString:@"?"];
                } else if (i > 0) {
                    [str appendString:@"&"];
                }
                NSString *name = [names objectAtIndex:i];
                [str appendString:[NSString stringWithFormat:@"%@=%@",
                                   name, [params objectForKey:name]]];
            }
        }
        
        NSLog(@"The Get Fullpath is %@", str);

        return str;
        
        
    }
	return fullPath;
    
}

- (void) getAsynchronousMethodUrl:(NSString *)relativeUrl  params:(NSDictionary *)params {
//    relativeUrl=[NSString stringWithFormat:@"%@%@",BASE_URL,relativeUrl];
//    NSURL *url=[NSURL URLWithString:relativeUrl];
//    NSString *postString=[self paramsFromDictionary:params];
//    NSLog(@"getString == %@", postString);
//    NSString *msgLength = [NSString stringWithFormat:@"%d", [postString length]];
//    NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] initWithURL:url] autorelease];
//    
//    [request addValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
//    [request addValue:msgLength forHTTPHeaderField:@"Content-Length"];
//    [request setHTTPMethod:@"GET"];
//    self.connection =  [NSURLConnection connectionWithRequest:request delegate:self];
//    
//    //防止子线程使用异步请求
//    while(!self.finished) {
//        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
//    }
    
    {
        NSString * encodedString = (NSString *)CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                                                       (CFStringRef)[self getURL:relativeUrl queryParameters:params],
                                                                                       NULL,
                                                                                       NULL,
                                                                                       kCFStringEncodingUTF8);
        
        
        
        NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] init] autorelease];
        NSLog(@"encodedString == %@",encodedString);
        [request setURL:[NSURL URLWithString:encodedString]];
        [request setHTTPMethod:@"GET"];
        
        [encodedString release];
        NSString *contentTypeLocal = [NSString stringWithFormat:@"text/json"];
        [request addValue:contentTypeLocal forHTTPHeaderField:@"Content-Type"];
        [request addValue:@"UTF-8" forHTTPHeaderField:@"charset"];
        self.connection =  [NSURLConnection connectionWithRequest:request delegate:self];
        
        //防止子线程使用异步请求
        while(!self.finished) {
            [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
        }
    }
    
    
    
    
    
 
}


- (void) postAsynchronousMethodUrlForTestServer:(NSString *)relativeUrl  params:(NSDictionary *)params {
    //bug bug fix
    relativeUrl=[NSString stringWithFormat:@"%@%@",BASE_URL,relativeUrl];
    NSURL *url=[NSURL URLWithString:relativeUrl];
    NSString *postString=[self paramsFromDictionary:params];
    NSString *msgLength = [NSString stringWithFormat:@"%d", [postString length]];
    NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] initWithURL:url] autorelease];
    
    [request addValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request addValue:msgLength forHTTPHeaderField:@"Content-Length"];
    [request setHTTPMethod:@"POST"];
    [request setHTTPBody: [postString dataUsingEncoding:NSUTF8StringEncoding]];
    request.timeoutInterval = 5.0;
    
    self.connection =  [NSURLConnection connectionWithRequest:request delegate:self];

    while(!self.finished) {
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
    }
}

- (void) postAsynchronousMethodUrl:(NSString *)relativeUrl  params:(NSDictionary *)params {
    relativeUrl=[NSString stringWithFormat:@"%@%@",BASE_URL,relativeUrl];
    NSURL *url=[NSURL URLWithString:relativeUrl];
    NSString *postString=[self paramsFromDictionary:params];
    NSLog(@"postString == %@", postString);
    NSString *msgLength = [NSString stringWithFormat:@"%d", [postString length]];
    NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] initWithURL:url] autorelease];
    
    [request addValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
    [request addValue:msgLength forHTTPHeaderField:@"Content-Length"];
    [request setHTTPMethod:@"POST"];
    [request setHTTPBody: [postString dataUsingEncoding:NSUTF8StringEncoding]];
    
    self.connection =  [NSURLConnection connectionWithRequest:request delegate:self];
    
    while(!self.finished) {
        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
    }
}


- (void) postAsynchronousMethodUrlForTestImageServer:(NSString *)relativeUrl  params:(NSDictionary *)params {
//    relativeUrl=[NSString stringWithFormat:@"%@%@",BASE_URL_IMAGE,relativeUrl];
//    NSURL *url=[NSURL URLWithString:relativeUrl];
//    NSString *postString=[self paramsFromDictionary:params];
//    NSLog(@"postString == %@", postString);
//    NSString *msgLength = [NSString stringWithFormat:@"%d", [postString length]];
//    NSMutableURLRequest *request = [[[NSMutableURLRequest alloc] initWithURL:url] autorelease];
//    
//    [request addValue:@"application/x-www-form-urlencoded" forHTTPHeaderField:@"Content-Type"];
//    [request addValue:msgLength forHTTPHeaderField:@"Content-Length"];
//    [request setHTTPMethod:@"POST"];
//    [request setHTTPBody: [postString dataUsingEncoding:NSUTF8StringEncoding]];   
//    
//    self.connection =  [NSURLConnection connectionWithRequest:request delegate:self];
//    
//    while(!self.finished) {
//        [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode beforeDate:[NSDate distantFuture]];
//    }
}


- (void) postAsynchronousMethodData:(NSString *)relativeUrl  data:(NSData *)data withtDictionary:(NSDictionary *)dic {
    NSString *rawBoundary = @"UNIQUE_BOUNDARY_FOR_IMAGE_DATA";
    //根据url初始化request
    NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@%@",BASE_URL,relativeUrl]]
                                                           cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                       timeoutInterval:10];
    //分界线 --UNIQUE_BOUNDARY_FOR_IMAGE_DATA
    NSString *MPboundary=[NSString stringWithFormat:@"--%@",rawBoundary];
    //结束符 UNIQUE_BOUNDARY_FOR_IMAGE_DATA--
    NSString *endMPboundary = [NSString stringWithFormat:@"%@--",MPboundary];
    //http body的字符串
    NSMutableString *body=[[[NSMutableString alloc]init] autorelease];
    //参数的集合的所有key的集合
    if (dic) {
        [dic enumerateKeysAndObjectsUsingBlock:^(id key, id obj, BOOL *stop) {
            //添加分界线，换行
            [body appendFormat:@"%@\r\n",MPboundary];
            //添加字段名称，换2行
            [body appendFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n",key];
            //添加字段的值
            [body appendFormat:@"%@\r\n",[dic objectForKey:key]];
        }];
    }
    
    //声明结束符：--UNIQUE_BOUNDARY_FOR_IMAGE_DATA--
    NSString *end=[NSString stringWithFormat:@"\r\n%@", endMPboundary];
    //声明myRequestData，用来放入http body
    NSMutableData *myRequestData = [NSMutableData data];


    if ([@"pic" isEqualToString:[dic valueForKey:@"messagetype"]]) {
        ////添加分界线，换行
        [body appendFormat:@"%@\r\n",MPboundary];
        //声明pic字段，文件名为headImage.png
        [body appendFormat:@"Content-Disposition: form-data; name=\"pic\"; filename=\"headImage.jpg\"\r\n"];
        //声明上传文件的格式
        [body appendFormat:@"Content-Type: image/png\r\n\r\n"];
    }
    else if ([@"text" isEqualToString:[dic valueForKey:@"messagetype"]]) {
        
    }
    else NSLog(@"MessageType Error! Cannot recoginize the type!");
    
    
    NSLog(@"body == %@", body);
    
    //将body字符串转化为UTF8格式的二进制
    [myRequestData appendData:[body dataUsingEncoding:NSUTF8StringEncoding]];
    if ([@"pic" isEqualToString:[dic valueForKey:@"messagetype"]]) {
        //将image的data加入
        [myRequestData appendData:data];
    }
    //加入结束符--UNIQUE_BOUNDARY_FOR_IMAGE_DATA--
    [myRequestData appendData:[end dataUsingEncoding:NSUTF8StringEncoding]];
    
    //设置HTTPHeader中Content-Type的值
    NSString *content=[[NSString alloc]initWithFormat:@"multipart/form-data; boundary=%@",rawBoundary];
    //设置HTTPHeader
    [request setValue:content forHTTPHeaderField:@"Content-Type"];
    //设置Content-Length
    [request setValue:[NSString stringWithFormat:@"%d", [myRequestData length]] forHTTPHeaderField:@"Content-Length"];
    //设置http body
    [request setHTTPBody:myRequestData];
    //http method
    [request setHTTPMethod:@"POST"];

    NSURLResponse **response = NULL;
    NSError *error;
    NSData *resultData = [NSURLConnection sendSynchronousRequest:request returningResponse:response error:&error];
//    self.connection =  [NSURLConnection connectionWithRequest:request delegate:self];
    
    NSLog(@"data == %@", [[NSString alloc] initWithData:resultData encoding:NSASCIIStringEncoding]);
}


- (void)_stopReceiveWithStatus:(NSString *)statusString {
    NSLog(@"statusString == %@", statusString);
    [self connectionClose];
    
    if ([delegate respondsToSelector:@selector(didReceiveStop:)]) {
        [delegate didReceiveStop:self];
    }
	
    [contentLength release];
    [statusText release];
    self.finished = YES;
}

- (void)connectionClose {
    [connection cancel];
    [connection release];
}


#pragma mark - NSURLConnectionDelegate Method
- (void)connection:(NSURLConnection *)theConnection didReceiveResponse:(NSURLResponse *)response {
    
    NSHTTPURLResponse * httpResponse;
    
    if(theConnection != self.connection) {
        return;
    } 
    
    httpResponse = (NSHTTPURLResponse *)response;
    
//    if([httpResponse isKindOfClass:[NSHTTPURLResponse class]]) {
//        return;
//    } 
    
    NSLog(@"the http response code is %d", httpResponse.statusCode);
    if ((httpResponse.statusCode / 100) != 2) {
		self.statusText = [NSString stringWithFormat:@"HTTP error %d", httpResponse.statusCode];
    }
	else {
        self.contentType = [httpResponse.allHeaderFields objectForKey:@"Content-Type"];
        self.contentLength = [httpResponse.allHeaderFields objectForKey:@"Content-Length"];
    }
    if([delegate respondsToSelector:@selector(didReceiveResponse:httpResponse:)]) {
        [delegate didReceiveResponse:self httpResponse:httpResponse];
    }
}

- (void)connection:(NSURLConnection *)theConnection didReceiveData:(NSData *)data {
	
    if(theConnection != self.connection) {
        return;
    } 
    
    [self.m_data appendData:data];
    
    if([delegate respondsToSelector:@selector(didReceiveData:data:)]) {
        [delegate didReceiveData:self data:data];
    }
}

- (void)connectionDidFinishLoading:(NSURLConnection *)theConnection {
    if(theConnection != self.connection) {
        return;
    } 
    
    [self _stopReceiveWithStatus:nil];
}

@end
