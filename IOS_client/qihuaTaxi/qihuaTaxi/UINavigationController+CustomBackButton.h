//
//  UINavigationController+CustomBackButton.h
//  qihuaTaxi
//
//  Created by 际航 杨 on 9/18/12.
//  Copyright (c) 2012 index informatics. All rights reserved.
//


#import <UIKit/UIKit.h>

@interface UINavigationController (CustomBackButton)

//在tabBarController之后的pushViewController使用本函数（导入本Category），功能：自定制了BackButton的样式
- (void)pushViewControllerWithCustomBackButton:(UIViewController *)viewController animated:(BOOL)animated;

@end
