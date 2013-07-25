//
//  MBProgressHUD+http.h
//  qihuaTaxi
//
//  Created by G K on 7/16/12.
//  Copyright (c) 2012 Index Informatics Limited. All rights reserved.
//

#import "MBProgressHUD.h"
#import "MBProgressHUD.h"
@interface MBProgressHUD (http)
#pragma mark - 
+ (void)usingHUDWithExcute:(void (^)(void))block;
+ (void)usingHUDWithText:(NSString*)labelText excute:(void (^)(void))block;
+ (void)usingHUDWithSuperview:(UIView *)superView excute:(void (^)(void))block;
+ (void)usingHUDWithSuperview:(UIView *)superView text:(NSString*)labelText excute:(void (^)(void))block;
#pragma mark -
+(void)dismiss;

@end
