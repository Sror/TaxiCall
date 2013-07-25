//
//  MainViewController.m
//  qihuaTaxi
//
//  Created by Alex on 6/6/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "HttpClientSampleViewController.h"

@interface HttpClientSampleViewController ()

@end

@implementation HttpClientSampleViewController


- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
}

- (void)viewDidUnload
{
    [super viewDidUnload];
    // Release any retained subviews of the main view.
}



#pragma mark - Flipside View Controller


- (void)dealloc
{
    [super dealloc];
}

- (IBAction)showInfo:(id)sender
{
    
}

- (IBAction)testHttp:(id)sender {
     httpClient=[[HttpClient alloc] initWithDelegate:self];
    [httpClient getHello:@"hello"];
}



-(void)doSuccess:(NSDictionary *)dict
{
    [httpClient release];
    NSLog(@"this is do success");
}
-(void)doFail:(NSString *)msg
{
    [httpClient release];
    NSLog(@"this is do fail");
}
-(void)doNetWorkFail
{
    [httpClient release];
    NSLog(@"This is NetWork fail");
}

@end
