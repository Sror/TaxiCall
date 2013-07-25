//
//  UINavigationController+CustomBackButton.m
//  qihuaTaxi
//
//  Created by 际航 杨 on 9/18/12.
//  Copyright (c) 2012 index informatics. All rights reserved.
//

#import "UINavigationController+CustomBackButton.h"
#import "UIViewController+CustomBackButton.h"

@implementation UINavigationController (CustomBackButton)

- (void)pushViewControllerWithCustomBackButton:(UIViewController *)viewController animated:(BOOL)animated {
    [self pushViewController:viewController animated:animated];
    [viewController addNavigationCutomizedBack];
}

@end
