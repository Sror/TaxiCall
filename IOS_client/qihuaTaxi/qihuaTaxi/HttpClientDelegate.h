//
//  HttpClientDelegate.h
//  qihuaTaxi
//
//  Created by G K on 3/15/12.
//  Copyright (c) 2012 Index Informatics Limited. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol HttpClientDelegate <NSObject>

@required
-(void)doSuccess:(NSDictionary *)dict;
-(void)doFail:(NSString *)msg;
@optional
-(void)doNetWorkFail;

@end
