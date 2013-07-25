//
//  UINavigationBar+dropshadow.m
//  qihuaTaxi
//
//  Created by Alex on 9/11/12.
//
//

#import "UINavigationBar+dropshadow.h"
#import <QuartzCore/QuartzCore.h>
@implementation UINavigationBar (dropshadow)
-(void)willMoveToWindow:(UIWindow *)newWindow{
	[super willMoveToWindow:newWindow];
	[self applyDefaultStyle];
}

- (void)applyDefaultStyle {
	// add the drop shadow
	self.layer.shadowColor = [[UIColor colorWithRed:50/255.0 green:38/255.0 blue:26/255.0 alpha:.7] CGColor];
	self.layer.shadowOffset = CGSizeMake(0.0, 0.8);
	self.layer.shadowOpacity = 0.4;
    self.layer.shadowRadius = 0.6;
    self.clipsToBounds = NO;
    
}
@end
