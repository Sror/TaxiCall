//
//  constant.h
//  qihuaTaxi
//
//  Created by Alex on 3/26/12.
//  Copyright (c) 2012 theindex. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DataSingleton.h"

#define REMOTE_USER_ID @"32"
#define SYSTEM_PRIVATE_MESSAGES @"private_message_in_store"
#define SYSTEM_VERSION @"1.1.0"



#define UMENG_APP_ID @"50bd7d7d52701564fb000003"
#define MEIZHOU_PUSH_SERVER_ID @"37"
#define APNS_URL @"219.237.242.71:8086/api"


///////////////////////////////////

#ifdef DEBUG
#define debugLog(...) NSLog(__VA_ARGS__)
#define debugMethod() NSLog(@"%s", __func__)
#else
#define debugLog(...)
#define debugMethod()
#endif

#define EMPTY_STRING        @""

#define STR(key)            NSLocalizedString(key, nil)

#define PATH_OF_APP_HOME    NSHomeDirectory()
#define PATH_OF_TEMP        NSTemporaryDirectory()
#define PATH_OF_DOCUMENT    [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0]







//////////////////////////////////////////////
#define NAV_ITEM_LEFT 1
#define NAV_ITEM_RIGHT 2
#define BUTTON_WIDTH 60.0
#define BUTTON_SEGMENT_WIDTH 51.0
#define CAP_WIDTH 15.0
typedef enum {
    CapLeft          = 0,
    CapMiddle        = 1,
    CapRight         = 2,
    CapLeftAndRight  = 3
} CapLocation;


////////////////////////////////////////////////
@interface constant : NSObject
{
    
}
+(UIImage*) drawGradientInRect:(CGSize)size withColors:(NSArray*)colors;

@end
