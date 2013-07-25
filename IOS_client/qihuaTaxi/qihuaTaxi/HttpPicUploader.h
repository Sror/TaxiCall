//
//  HttpPicUploader.h
//  qihuaTaxi
//
//  Created by Alex on 6/6/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <zlib.h>
@interface HttpPicUploader : NSObject {
    NSURL *serverURL;
    NSString *filePath;
    id delegate;
    SEL doneSelector;
    SEL errorSelector;
    
    BOOL uploadDidSucceed;
}

-(id)initWithURL: (NSURL *)serverURL 
           filePath: (NSString *)filePath 
           delegate: (id)delegate 
       doneSelector: (SEL)doneSelector 
      errorSelector: (SEL)errorSelector;

- (NSString *)filePath;

@end

/*
 [[Uploader alloc] initWithURL:[NSURL [[URLWithString]]:@"http://my-server.com/uploader.php"
 filePath:@"/Users/someone/foo.jpg"
 delegate:self
 doneSelector:@selector(onUploadDone:)
 errorSelector:@selector(onUploadError:)];
*/