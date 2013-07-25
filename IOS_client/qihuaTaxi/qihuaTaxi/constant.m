//
//  constant.m
//  qihuaTaxi
//
//  Created by Alex on 3/26/12.
//  Copyright (c) 2012 theindex. All rights reserved.
//

#import "constant.h"
#import "DataSingleton.h"
#import <QuartzCore/QuartzCore.h>
#import <CommonCrypto/CommonDigest.h>
/////////////////////////////////////////////////
@implementation constant


+(UIBarButtonItem*)woodBarButtonItemWithText:(NSString*)buttonText addTarget:(id)navigationBar selector:(SEL)mypressfunction type:(NSInteger)leftOrRight
{
    return [[[UIBarButtonItem alloc] initWithCustomView:[self woodButtonWithText:buttonText stretch:CapLeftAndRight addTarget:navigationBar selector:mypressfunction type:leftOrRight]] autorelease];
}




+(UIButton*)woodButtonWithText:(NSString*)buttonText stretch:(CapLocation)location addTarget:(id)navigationBar selector:(SEL)mypress type:(NSInteger)leftOrRight
{
    UIImage* buttonImage = nil;
    UIImage* buttonPressedImage = nil;
    NSUInteger buttonWidth = 0;
    if (location == CapLeftAndRight)
    {
        if (buttonText.length <= 6) {
            buttonWidth = BUTTON_WIDTH;
        }else
        {
            buttonWidth = BUTTON_WIDTH + (buttonText.length -6 )*8;
        }
        
        
        if (leftOrRight == NAV_ITEM_LEFT) {
            buttonImage = [[UIImage imageNamed:@"navigationBarBackButton.png"] stretchableImageWithLeftCapWidth:CAP_WIDTH topCapHeight:0.0];
            buttonPressedImage = [[UIImage imageNamed:@"navigationBarBackButton.png"] stretchableImageWithLeftCapWidth:CAP_WIDTH topCapHeight:0.0];
        }else
        {
            buttonImage = [[UIImage imageNamed:@"nav-button.png"] stretchableImageWithLeftCapWidth:CAP_WIDTH topCapHeight:0.0 ];
            buttonPressedImage = [[UIImage imageNamed:@"nav-button.png"] stretchableImageWithLeftCapWidth:CAP_WIDTH topCapHeight:0.0 ];
        }
        
    }else{
        buttonImage=[UIImage imageNamed:@""];
    }
    
    
    
    UIButton* button = [UIButton buttonWithType:UIButtonTypeCustom];
    button.frame = CGRectMake(0.0, 0.0, buttonWidth, buttonImage.size.height);
    button.titleLabel.font = [UIFont boldSystemFontOfSize:[UIFont smallSystemFontSize]];
    button.titleLabel.textColor = [UIColor whiteColor];
    button.titleLabel.shadowOffset = CGSizeMake(0,-1);
    button.titleLabel.shadowColor = [UIColor darkGrayColor];
    
    [button setTitle:buttonText forState:UIControlStateNormal];
    [button setBackgroundImage:buttonImage forState:UIControlStateNormal];
    [button setBackgroundImage:buttonPressedImage forState:UIControlStateHighlighted];
    [button setBackgroundImage:buttonPressedImage forState:UIControlStateSelected];
    button.adjustsImageWhenHighlighted = NO;
    
    [button addTarget:navigationBar action:mypress forControlEvents:UIControlEventTouchUpInside];
    
    return button;
}




+(double)caculateDistance:(float)x1 withY1:(float)y1 withX2:(float)x2 withY2:(float)y2
{
    double dDistance = 0.0;
    //-------------------------------------------------------------------
    double dRadLatitude1 = x1*3.1415926/180.0;
    double dRadLatitude2 = x2*3.1415926/180.0;
    double dDisLatitude = dRadLatitude1 - dRadLatitude2;
    double dDisLongitude = y1*3.1415926/180.0 - y2*3.1415926/180.0;
    
    dDistance = 2 * asin(sqrt(pow(sin(dDisLatitude/2),2) + cos(dRadLatitude1)*cos(dRadLatitude2)*pow(sin(dDisLongitude/2),2)));
    dDistance *= 6378.137;
    //-------------------------------------------------------------------
    return dDistance;
}


+(void)setNormalshadow:(UIView*)inputUIview
{
    [[inputUIview layer] setShadowOffset:CGSizeMake(0, 0)];
    [[inputUIview layer] setShadowRadius:2];
    [[inputUIview layer] setShadowOpacity:1];
    [[inputUIview layer] setShadowColor:[UIColor blackColor].CGColor];
}


+(UIImage*) drawGradientInRect:(CGSize)size withColors:(NSArray*)colors {
    NSMutableArray *ar = [NSMutableArray array];
    for(UIColor *c in colors) [ar addObject:(id)c.CGColor];
    
    UIGraphicsBeginImageContextWithOptions(size, YES, 1);
    
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSaveGState(context);
    
    //CGColorSpaceRef colorSpace = CGColorGetColorSpace([[colors lastObject] CGColor]);
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGGradientRef gradient = CGGradientCreateWithColors(colorSpace, (CFArrayRef)ar, NULL);
    
    //     CGContextClipToRect(context, rect);
    
    CGPoint start = CGPointMake(0.0, 0.0);
    CGPoint end = CGPointMake(0.0, size.height);
    
    //CGContextDrawLinearGradient(context, gradient, start, end, kCGGradientDrawsBeforeStartLocation | kCGGradientDrawsAfterEndLocation);
    CGContextDrawLinearGradient(context, gradient, start, end, 0);
    
    
    UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
    
    CGGradientRelease(gradient);
    CGContextRestoreGState(context);
    
    // Clean up
    CGColorSpaceRelease(colorSpace); // Necessary?
    UIGraphicsEndImageContext(); // Clean up
    return image;
}

@end

/////////////////////////////////////////////////


