//
//  AboutViewController.m
//  qihuaTaxi
//
//  Created by Apple on 12-12-5.
//  Copyright (c) 2012年 Alex. All rights reserved.
//

#import "AboutViewController.h"

@interface AboutViewController ()

@end

@implementation AboutViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}
-(void)setButtonStyple:(UIGlossyButton*)b
{
    
	[b useWhiteLabel: YES];
    b.buttonCornerRadius = 4.0;
    b.buttonBorderWidth = 1.0f;
	[b setStrokeType: kUIGlossyButtonStrokeTypeBevelUp];
    b.tintColor = b.borderColor = [UIColor colorWithRed:130/255.0f green:168/255.0f blue:34.0f/255.0f alpha:1.0f];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.view.backgroundColor =  [UIColor colorWithPatternImage:[UIImage imageNamed:@"background.png"]];
    
    self.statementLabel.shadowColor = [UIColor colorWithWhite:1.0f alpha:0.8f];
    self.statementLabel.shadowOffset = CGSizeMake(1.0f, 2.0f);
    self.statementLabel.shadowBlur = 1.0f;
    self.statementLabel.innerShadowColor = [UIColor colorWithWhite:0.0f alpha:0.8f];
    self.statementLabel.innerShadowOffset = CGSizeMake(1.0f, 2.0f);

 
    
    [self setButtonStyple:self.termOfServerButton];
    [self setButtonStyple:self.orderingButton];
    
    
    
     self.button3.backgroundColor= [UIColor colorWithPatternImage:[UIImage imageNamed:@"Callingbar.png"]];
    
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)viewDidUnload {
    [self setStatementLabel:nil];
    [self setTermOfServerButton:nil];
    [self setOrderingButton:nil];
    [self setButton3:nil];
    [super viewDidUnload];
}
- (IBAction)startTermOfservice:(id)sender {

}


- (IBAction)startCall:(id)sender {
    NSURL* url = [[NSURL alloc] initWithString:@"tel:96106"] ;
    [[ UIApplication sharedApplication]openURL:url];
}
@end
