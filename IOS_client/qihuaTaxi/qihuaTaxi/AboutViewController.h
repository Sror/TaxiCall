//
//  AboutViewController.h
//  qihuaTaxi
//
//  Created by Apple on 12-12-5.
//  Copyright (c) 2012年 Alex. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "FXLabel.h"
#import "UIGlossyButton.h"
@interface AboutViewController : UIViewController{
    
}
@property (weak, nonatomic) IBOutlet FXLabel *statementLabel;
@property (weak, nonatomic) IBOutlet UIGlossyButton *termOfServerButton;


@property (weak, nonatomic) IBOutlet UIGlossyButton *orderingButton;

- (IBAction)startTermOfservice:(id)sender;
- (IBAction)startCall:(id)sender;

@property (weak, nonatomic) IBOutlet UIButton *button3;



@end
