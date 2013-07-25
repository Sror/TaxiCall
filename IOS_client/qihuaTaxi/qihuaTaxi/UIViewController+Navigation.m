
//
//  UIViewController+Navigation.m
//  qihuaTaxi
//
//  Created by 际航 杨 on 6/13/12.
//  Copyright (c) 2012 index informatics. All rights reserved.
//

#import "UIViewController+Navigation.h"

@implementation UIViewController (Navigation)


-(UINavigationController *)navigationController;
{
    return (UINavigationController *)[UIApplication sharedApplication].keyWindow.rootViewController;
}

@end