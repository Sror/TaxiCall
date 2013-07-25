//
//  WeiboConnection.h
//  qihuaTaxi
//
//  Created by junmin liu on 10-11-20.
//  Copyright 2010 Openlab. All rights reserved.
//

#import <Foundation/Foundation.h>



#define BASE_URL @""

@interface HttpConnection : NSObject {
}




- (NSString *) getMethodUrl:(NSString *)relativeUrl
                     params:(NSMutableDictionary *)params;

- (NSString *) postMethodUrl:(NSString *)url
           params:(NSMutableDictionary *)params;

-(NSString *) getURL:(NSString *)url queryParameters:(NSMutableDictionary *)params;
-(NSString *)paramsFromDictionary:(NSMutableDictionary *)params;



@end
