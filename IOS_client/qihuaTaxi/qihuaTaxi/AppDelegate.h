//
//  AppDelegate.h
//  qihuaTaxi
//
//  Created by Alex on 12/3/12.
//  Copyright (c) 2012 Alex. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HttpClient.h"
@interface AppDelegate : UIResponder <UIApplicationDelegate, UITabBarControllerDelegate,HttpClientDelegate>
{
    
}
@property (strong, nonatomic) UIWindow *window;

@property (strong, nonatomic) UITabBarController *tabBarController;
@property (strong, nonatomic) UINavigationController* navigationController;

@end
