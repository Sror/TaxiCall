//
//  MBProgressHUD+http.m
//  qihuaTaxi
//
//  Created by G K on 7/16/12.
//  Copyright (c) 2012 Index Informatics Limited. All rights reserved.
//

#import "MBProgressHUD+http.h"
static MBProgressHUD    *hud;
@implementation MBProgressHUD (http)

+ (void)usingHUDWithExcute:(void (^)(void))block
{
    [self usingHUDWithText: NSLocalizedString(@"Wait...",nil) excute:block];
}
+ (void)usingHUDWithText:(NSString*)labelText excute:(void (^)(void))block
{
    [self usingHUDWithSuperview:[UIApplication sharedApplication].keyWindow text:labelText excute:block];
}
+ (void)usingHUDWithSuperview:(UIView *)superView excute:(void (^)(void))block
{
    [self usingHUDWithSuperview:superView text:@"Wait..." excute:block];
}

+ (void)usingHUDWithSuperview:(UIView *)superView text:(NSString*)labelText excute:(void (^)(void))block
{
    if (hud&&hud.superview) {
        [hud hide:YES];
        [hud removeFromSuperview];
        hud=nil;
    }
    hud=[self showHUDAddedTo:superView animated:YES];
    hud.labelText=labelText;
    [hud show:YES];
    double delayInSeconds = 0.2;
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, delayInSeconds * NSEC_PER_SEC);
    dispatch_after(popTime, dispatch_get_main_queue(), ^(void){
        block();
    });
}

+(void)dismiss
{
    if (hud!=nil&&hud.superview) {
        [hud hide:YES];
        [hud removeFromSuperview];
        hud=nil;
    }
}
@end
