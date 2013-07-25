//
//  AppDelegate.m
//  qihuaTaxi
//
//  Created by Alex on 12/3/12.
//  Copyright (c) 2012 Alex. All rights reserved.
//

#import "AppDelegate.h"
#import "MapKitDragAndDropViewController.h"
#import "FirstViewController.h"
#import "MobClick.h"
#import "SecondViewController.h"

@implementation AppDelegate
@synthesize navigationController = _navigationController;
static void uncaughtExceptionHandler(NSException *exception) {
    
    NSLog(@"CRASH: %@", exception);
    
    NSLog(@"Stack Trace: %@", [exception callStackSymbols]);
    
    // Internal error reporting
    
}
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    
    NSSetUncaughtExceptionHandler(&uncaughtExceptionHandler);

    [MobClick startWithAppkey:UMENG_APP_ID reportPolicy:REALTIME channelId:nil];
    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
    UIViewController  *viewController2;
    if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
        viewController2 = [[SecondViewController alloc] initWithNibName:@"SecondViewController_iPhone" bundle:nil];
    } else {
        viewController2 = [[SecondViewController alloc] initWithNibName:@"SecondViewController_iPad" bundle:nil];
    }
    self.tabBarController = [[UITabBarController alloc] init];
    self.tabBarController.viewControllers = @[ viewController2];
    
    
    UIViewController* viewcontroller3 = [[MapKitDragAndDropViewController alloc] initWithNibName:@"MapKitDragAndDropViewController" bundle:nil];
    _navigationController = [[UINavigationController alloc] initWithRootViewController:viewcontroller3];
    self.window.rootViewController = self.navigationController;
    //    [_navigationController release];
    
    if([self.navigationController.navigationBar respondsToSelector:@selector(setBackgroundImage:forBarMetrics:)] ) {
        [self.navigationController.navigationBar setBackgroundImage:[UIImage imageNamed:@"NBan.png"] forBarMetrics: UIBarMetricsDefault];
    }
    
    NSDictionary *dict = [NSDictionary dictionaryWithObjectsAndKeys:
                          [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1],UITextAttributeTextColor,
                          [NSValue valueWithUIOffset:UIOffsetMake(0, 0)], UITextAttributeTextShadowOffset,nil];
    
    
    self.navigationController.navigationBar.titleTextAttributes = dict;
    
    NSMutableArray* colorArray = [[NSMutableArray alloc] init];
    [colorArray addObject:[UIColor colorWithRed:0/255.0f green:0/255.0f blue:0/255.0f alpha:1.0f]];
    [colorArray addObject:[UIColor colorWithRed:148/255.0f green:190/255.0f blue:22/255.0f alpha:1.0f]];
    //[colorArray addObject:[UIColor colorWithRed:0/255.0f green:0/255.0f blue:22/255.0f alpha:1.0f]];
    
    
    UIImage* Temp = [constant drawGradientInRect:CGSizeMake(320, 44) withColors:colorArray];
    
    
       
    
    

     self.navigationController.navigationBar.tintColor=[UIColor colorWithRed:27/255.0 green:111/255.0 blue:30/255.0 alpha:1.0];
    [self.navigationController.navigationBar setBackgroundImage:[UIImage imageNamed:@"nbarBackground.png"] forBarMetrics: UIBarMetricsDefault];

    [self.window makeKeyAndVisible];

    
    
    self.window.rootViewController = self.navigationController;
    [self.window makeKeyAndVisible];
    
    
    
    {
        HttpClient* httpclient = [[HttpClient alloc] initWithDelegate:self];
        //[httpclient getPolicyState];
        
    }
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

-(void)doSuccess:(NSDictionary *)dict
{
    
}
-(void)doFail:(NSString *)msg
{
    NSLog(@"this is do fail: %@", msg);
    
    
    //even fail , we should reload.
    
}

-(void)doNetWorkFail
{
    NSLog(@"This is NetWork fail");
}

@end
