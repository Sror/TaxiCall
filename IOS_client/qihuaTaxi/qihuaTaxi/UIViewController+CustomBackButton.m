//
//  UIViewController+CustomBackButton.m
//  qihuaTaxi
//
//  Created by 际航 杨 on 9/18/12.
//  Copyright (c) 2012 index informatics. All rights reserved.
//

#import "UIViewController+CustomBackButton.h"
#import "UIViewController+Navigation.h"
#import "constant.h"
#import <QuartzCore/QuartzCore.h>

@implementation UIViewController (CustomBackButton)

-(void)addNavigationCutomizedBack {
    UIButton* backButton =[[[UIButton alloc] initWithFrame:CGRectMake(0, 0, 63, 42)] autorelease];
    
    UIImage*buttonImage = [[UIImage imageNamed:@"back_button.png"] stretchableImageWithLeftCapWidth:CAP_WIDTH topCapHeight:0.0];
    [backButton setBackgroundImage:buttonImage forState:UIControlStateNormal];
    backButton.titleLabel.font = [UIFont boldSystemFontOfSize:[UIFont smallSystemFontSize]];
    backButton.titleLabel.textColor = [UIColor whiteColor];
    backButton.titleLabel.shadowOffset = CGSizeMake(0,-1);
    backButton.titleLabel.shadowColor = [UIColor darkGrayColor];
    backButton.backgroundColor = [UIColor clearColor];
    [backButton setTitle:@"  " forState:UIControlStateNormal];
    backButton.layer.cornerRadius = 5.0;
    [backButton addTarget:self action:@selector(backAction) forControlEvents:UIControlEventTouchUpInside];
    
    UIBarButtonItem *backItems = [[[UIBarButtonItem alloc] initWithCustomView:backButton ] autorelease];
    
    self.navigationController.navigationItem.leftBarButtonItem = backItems;
    self.navigationItem.leftBarButtonItem = backItems;
}

-(void)backAction {
    [self.navigationController setToolbarHidden:YES];
    [self.navigationController popViewControllerAnimated:YES];
}

@end
