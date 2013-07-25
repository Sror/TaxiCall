//
//  MainViewController.h
//  qihuaTaxi
//
//  Created by Alex on 6/6/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//#import "FlipsideViewController.h"
#import "HttpClient.h"

@interface HttpClientSampleViewController : UIViewController <HttpClientDelegate>
{
    HttpClient                      *httpClient;
}

- (IBAction)showInfo:(id)sender;
- (IBAction)testHttp:(id)sender;

@end
