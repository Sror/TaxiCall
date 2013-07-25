//
//  AppDelegate.h
//  qihuaTaxi
//
//  Created by Alex on 3/22/12.
//  Copyright (c) 2012 theindex. All rights reserved.
//

#import "DataSingleton.h"
#import "constant.h"
@implementation DataSingleton


static DataSingleton * MyCommon_Singleton = nil;

+ (DataSingleton *)singleton

{
    
    @synchronized(self)
    {
        if  (MyCommon_Singleton  ==  nil)
        {
            MyCommon_Singleton=[[[DataSingleton alloc] init] autorelease];
            
           
        }
    }
    
    return  MyCommon_Singleton;
    
}
+  (id)allocWithZone:(NSZone  * )zone
{
    @synchronized(self)
    {
        if (MyCommon_Singleton  ==  nil)
        {
            MyCommon_Singleton  =  [super allocWithZone:zone];
            return  MyCommon_Singleton;
        }
    }
    
    return  nil;
}


- (id)copyWithZone:(NSZone *)zone
{
    return self;
}

//retain返回单例本身
- (id)retain
{
    return self;
}

//引用计数总是为1
- (unsigned)retainCount
{
    return 1;
}

//release不做任何处理
- (oneway void)release
{
    
}

//autorelease返回单例本身
- (id)autorelease
{
    return self;
}

//真release私有接口
-(void)realRelease
{
    [super release];
}


-(void)dealloc
{
   [super dealloc];
}






@end
