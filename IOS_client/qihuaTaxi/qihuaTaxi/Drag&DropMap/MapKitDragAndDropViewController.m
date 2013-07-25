//
//  
//  qihuaTaxi
//
//  Created by Alex on 3/22/12.
//  Copyright (c) 2012 theindex. All rights reserved.


#import "MapKitDragAndDropViewController.h"
#import "DDAnnotation.h"
#import "DDAnnotationView.h"
#import "MKMapView+ZoomLevel.h"
#import "../DataSingleton.h"
#import <QuartzCore/QuartzCore.h>
#import "UIView+LayerEffects.h"
#import "AboutViewController.h"
#import "FXLabel.h"
@interface MapKitDragAndDropViewController () 
- (void)coordinateChanged_:(NSNotification *)notification;
@end

@implementation MapKitDragAndDropViewController
@synthesize LatString,LonString;
@synthesize mapView;
@synthesize needSendingLocationMap;
@synthesize orderStateHintLabel;
@synthesize bottomBackgroundboard;
#define MAP_DEFAULT_ZOOM_LEVEL 12
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.needSendingLocationMap = 0;
        LatString = [[NSMutableString alloc] init] ;
        LonString = [[NSMutableString alloc] init] ;
        orderStateHintLabel.text = @"正在更新您当前的位置信息";
        self.title = @"奇华约车";
    }
    return self;
}


-(void)leftButtonAction
{
    AboutViewController* aboutViewer = [[[AboutViewController alloc] initWithNibName:@"AboutViewController" bundle:nil] autorelease];
    [self.navigationController pushViewController:aboutViewer animated:YES];
}
-(void)rightButtonAction
{
    ///1 : is refresh, will call the animation.
    [self SendRequest:1];
    
}

-(void)startPhoneCall
{
    NSURL* url = [[[NSURL alloc] initWithString:@"tel:13911650018"] autorelease];
    [[ UIApplication sharedApplication]openURL:url];
}


-(void)setButtonStyple:(UIButton*)b
{
   
	[b useWhiteLabel: YES];
    //b.layer CornerRadius = 4.0;
    
    NSMutableArray* colorArray = [[NSMutableArray alloc] init];
    
    [colorArray addObject:[UIColor colorWithRed:118/255.0f green:157/255.0f blue:0/255.0f alpha:1.0f]];
    [colorArray addObject:[UIColor colorWithRed:148/255.0f green:190/255.0f blue:22/255.0f alpha:1.0f]];
    
    UIImage* Temp = [constant drawGradientInRect:b.frame.size withColors:colorArray];
    b.tintColor =[UIColor colorWithPatternImage:Temp];
    
   
    
}
// Implement viewDidLoad to do additional setup after loading the view, typically from a nib.
-(void)setLabelInnerShadow:(FXLabel*)label1
{
    
    FXLabel* label2= (FXLabel*)label1;
    label2.textColor =  [UIColor colorWithRed:153/255.0f green:153/255.0f blue:153.0f/255.0f alpha:1.0f];

    label2.shadowColor = [UIColor colorWithWhite:1.0f alpha:0.8f];
    label2.shadowOffset = CGSizeMake(1.0f, 2.0f);
    label2.shadowBlur = 1.0f;
    label2.innerShadowColor = [UIColor colorWithWhite:0.0f alpha:0.8f];
    label2.innerShadowOffset = CGSizeMake(1.0f, 2.0f);
}
-(void)addMBprogressHUD
{
    HUD = [[MBProgressHUD alloc] initWithView:self.view];
    [self.view addSubview:HUD];
    
    HUD.delegate = self;
    HUD.labelText = @"查询数据中";
    [HUD setHidden:YES];
}
- (void)viewDidLoad {
    
    [super viewDidLoad];
    [self addMBprogressHUD];
	[self addNavigationItems];

    self.view.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"grayBackground.jpg"]];

    self.myScrollView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"background.png"]];
    
    
    self.divideImage1.image = [UIImage imageNamed:@"Dividingline1.png"];
    self.divideImage2.image = [UIImage imageNamed:@"Dividingline1.png"];
    self.divideImage3.image = [UIImage imageNamed:@"Dividingline2.png"];
    
    [self setButtonStyple:self.phoneNumberButton1];
    [self.phoneNumberButton1 setTitle:@"+86 139 **** 1234" forState:UIControlStateNormal];
    
    
    [self setButtonStyple:self.phoneNumberButton2];
    [self.phoneNumberButton2 setTitle:@"+86 189 **** 1234" forState:UIControlStateNormal];
    
 
    
    ///default location.
	CLLocationCoordinate2D theCoordinate;
	theCoordinate.latitude = 39.912534;
    theCoordinate.longitude = 116.454091;
	
	DDAnnotation *annotation = [[[DDAnnotation alloc] initWithCoordinate:theCoordinate addressDictionary:nil] autorelease];
	annotation.title = @"按住图钉，拖动编注位置";
	annotation.subtitle = [NSString	stringWithFormat:@"%f %f", annotation.coordinate.latitude, annotation.coordinate.longitude];
	
	[self.mapView addAnnotation:annotation];	
    
    self.mapView.showsUserLocation = YES;
    double lat = [self.mapView.userLocation coordinate].latitude;
    double lon = [self.mapView.userLocation coordinate].longitude;
    if (ABS(lat - (-180.0)) < 0.005 && ABS(lon -(-180.0) < 0.005 )) {
        [self.mapView setCenterCoordinate:theCoordinate zoomLevel:MAP_DEFAULT_ZOOM_LEVEL animated:NO];
    }
    else {
        //theCoordinate
        [self.mapView setCenterCoordinate:[self.mapView.userLocation coordinate] zoomLevel:MAP_DEFAULT_ZOOM_LEVEL animated:NO];
        
    }
    
    
    
    
    locationManager = [[CLLocationManager alloc] init];//创建位置管理器
    
    locationManager.delegate=self;//设置代理
    
    locationManager.desiredAccuracy=kCLLocationAccuracyBest;//指定需要的精度级别
    locationManager.distanceFilter=100.0f;//设置距离筛选器
    [locationManager startUpdatingLocation];//启动位置管理器
    
    singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(singleTapMapView)] ;
    [mapView addGestureRecognizer:singleTap];
    mapView.userInteractionEnabled = NO;
    
    UITapGestureRecognizer*singleTapScrollView = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(singleTapScrollViewAction:)] ;
    [self.bottomBackgroundboard addGestureRecognizer:singleTapScrollView];
    self.bottomBackgroundboard.userInteractionEnabled = YES;
    
    //to do huangzf
    //[[mapView layer] setBorderWidth:2];
    //[[mapView layer] setBorderColor:[UIColor blackColor].CGColor];
    
    
    {
        self.orderStateHintLabel.textColor =  [UIColor colorWithRed:102/255.0f green:102/255.0f blue:102/255.0f alpha:1.0f];
        self.bottomBackgroundboard.backgroundColor = [UIColor colorWithRed:32/255.0f green:118/255.0f blue:61.0f/255.0f alpha:1.0f];
        
        [self setLabelInnerShadow:self.taxiInfoLabelvalue1];
        [self setLabelInnerShadow:self.taxiInfoLabelvalue2];
        [self setLabelInnerShadow:self.taxiInfoLabelvalue3];
        [self setLabelInnerShadow:self.taxiInfoLabelvalue4];
        [self setLabelInnerShadow:self.taxiInfoLabelvalue5];

        self.hintTitle.textColor =  [UIColor colorWithRed:126/255.0f green:179/255.0f blue:25/255.0f alpha:1.0f];
        self.hintContent.textColor =  [UIColor colorWithRed:153/255.0f green:153/255.0f blue:153/255.0f alpha:1.0f];

        self.taxiInfoLabel1.textColor =  [UIColor colorWithRed:128/255.0f green:128/255.0f blue:128/255.0f alpha:1.0f];
        self.taxiInfoLabel2.textColor =  [UIColor colorWithRed:128/255.0f green:128/255.0f blue:128/255.0f alpha:1.0f];
        self.taxiInfoLabel3.textColor =  [UIColor colorWithRed:128/255.0f green:128/255.0f blue:128/255.0f alpha:1.0f];
        self.taxiInfoLabel4.textColor =  [UIColor colorWithRed:128/255.0f green:128/255.0f blue:128/255.0f alpha:1.0f];
        self.taxiInfoLabel5.textColor =  [UIColor colorWithRed:128/255.0f green:128/255.0f blue:128/255.0f alpha:1.0f];
        
        
        
        
        
        
        //self.taxiInfoLabelvalue1.text = @"北京";
    }

    [self cutomizeMapBackgroundBoard];
    NSLog(@"the gesture Recognizers are %@",mapView.gestureRecognizers);
}


-(void)cutomizeMapBackgroundBoard
{
    UIView* line1View = [[[UIView alloc] initWithFrame:CGRectMake(0, 0, 320, 1)] autorelease];
    UIView* line2View =[[[UIView alloc] initWithFrame:CGRectMake(0, 1, 320, 1)] autorelease];
    line1View.backgroundColor = [UIColor colorWithRed:0/255.0f green:143/255.0f blue:31/255.0f alpha:1.0f];
    line2View.backgroundColor = [UIColor colorWithRed:0/255.0f green:71/255.0f blue:15/255.0f alpha:1.0f];
    
    [self.bottomBackgroundboard addSubview:line1View];
    [self.bottomBackgroundboard addSubview:line2View];
    
    
    self.bottomStateLabel.font = [UIFont boldSystemFontOfSize:14];
    self.bottomStateLabel.textColor = [UIColor colorWithRed:242/255.0f green:242/255.0f blue:242/255.0f alpha:1.0f];
    
    NSMutableArray* colorArray = [[NSMutableArray alloc] init];
    [colorArray addObject:[UIColor colorWithRed:28/255.0f green:120/255.0f blue:59/255.0f alpha:1.0f]];
    //[colorArray addObject:[UIColor colorWithRed:255/255.0f green:255/255.0f blue:255/255.0f alpha:1.0f]];
    [colorArray addObject:[UIColor colorWithRed:20/255.0f green:107/255.0f blue:53/255.0f alpha:1.0f]];
        
    UIImage* Temp = [constant drawGradientInRect:bottomBackgroundboard.frame.size withColors:colorArray];
    
    bottomBackgroundboard.backgroundColor =[UIColor colorWithPatternImage:Temp];


}

-(void)SendRequest:(int)isFresh
{
    if (LatString.length > 0 && LonString.length > 0) {
        
        [HUD show:YES];
        [HUD setHidden:NO];
        
        if (isFresh) {
            ///do some animation here
            [UIView animateWithDuration:0.1 animations:^{
               
                
            } completion:^(BOOL finished) {
                [UIView animateWithDuration:0.1 animations:^{
                    
                }];
            }];
            
        }

        
        HttpClient* httpclient =[[[HttpClient alloc] initWithDelegate:self] autorelease];
        [httpclient sendTaxiOrderRequest:LatString withLonstring:LonString ];
        
        
                
    }
    else{
        
    }
    
}



- (void)locationManager:(CLLocationManager *)manager 
    didUpdateToLocation:(CLLocation *)newLocation 
           fromLocation:(CLLocation *)oldLocation
{
  ;
   
    
    self.LatString =  [@"" stringByAppendingFormat:@"%f",newLocation.coordinate.latitude ];
    self.LonString =  [@"" stringByAppendingFormat:@"%f",newLocation.coordinate.longitude ];
     
    [self.mapView setCenterCoordinate:newLocation.coordinate zoomLevel:MAP_DEFAULT_ZOOM_LEVEL animated:NO];
    
}
//位置查询遇到错误时调用这个方法
-(void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error
{
    NSString *errorType = (error.code == kCLErrorDenied) ? 
    @"拒绝访问" : @"未知错误";
    UIAlertView *alert = [[UIAlertView alloc] 
                      initWithTitle:@"获取位置失败"
                      message:errorType 
                      delegate:nil 
                      cancelButtonTitle:@"好的"
                      otherButtonTitles:nil];
    [alert show];
    [alert release];
}


- (void)viewWillAppear:(BOOL)animated {
	
	[super viewWillAppear:animated];
	
	// NOTE: This is optional, DDAnnotationCoordinateDidChangeNotification only fired in iPhone OS 3, not in iOS 4.
	[[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(coordinateChanged_:) name:@"DDAnnotationCoordinateDidChangeNotification" object:nil];
    
   }

- (void)viewWillDisappear:(BOOL)animated {
	
	[super viewWillDisappear:animated];
	
	// NOTE: This is optional, DDAnnotationCoordinateDidChangeNotification only fired in iPhone OS 3, not in iOS 4.
	[[NSNotificationCenter defaultCenter] removeObserver:self name:@"DDAnnotationCoordinateDidChangeNotification" object:nil];	
}


- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
    [self setHintContent:nil];
    [self setHintTitle:nil];
    [self setBottomStateLabel:nil];
    [self setBottomBackgroundboard:nil];
    [self setTaxiInfoLabelvalue5:nil];
    [self setTaxiInfoLabel5:nil];
    [self setTaxiInfoLabelvalue4:nil];
    [self setTaxiInfoLabel4:nil];
    [self setTaxiInfoLabelvalue3:nil];
    [self setTaxiInfoLabel3:nil];
    [self setTaxiInfoLabelvalue2:nil];
    [self setTaxiInfoLabel2:nil];
    [self setTaxiInfoLabelvalue1:nil];
    [self setTaxiInfoLabel1:nil];
    [self setOrderStateHintLabel:nil];
    [self setPhoneNumberButton2:nil];
    [self setPhoneNumberButton1:nil];
    [self setDivideImage3:nil];
    [self setDivideImage2:nil];
    [self setDivideImage1:nil];
    [self setMyScrollView:nil];
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
	
	self.mapView.delegate = nil;
	self.mapView = nil;
}

- (void)dealloc {
	mapView.delegate = nil;
	[mapView release];
    [super dealloc];
}

#pragma mark -
#pragma mark DDAnnotationCoordinateDidChangeNotification

// NOTE: DDAnnotationCoordinateDidChangeNotification won't fire in iOS 4, use -mapView:annotationView:didChangeDragState:fromOldState: instead.
- (void)coordinateChanged_:(NSNotification *)notification {
	
	DDAnnotation *annotation = notification.object;
	annotation.subtitle = [NSString	stringWithFormat:@"%f %f", annotation.coordinate.latitude, annotation.coordinate.longitude];
}

#pragma mark -
#pragma mark MKMapViewDelegate

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)annotationView didChangeDragState:(MKAnnotationViewDragState)newState fromOldState:(MKAnnotationViewDragState)oldState {
	
	if (oldState == MKAnnotationViewDragStateDragging) {
		DDAnnotation *annotation = (DDAnnotation *)annotationView.annotation;
		annotation.subtitle = [NSString	stringWithFormat:@"%f %f", annotation.coordinate.latitude, annotation.coordinate.longitude];		
	}
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id <MKAnnotation>)annotation {
	
    if ([annotation isKindOfClass:[MKUserLocation class]]) {
        return nil;		
	}
	
	static NSString * const kPinAnnotationIdentifier = @"PinIdentifier";
	MKAnnotationView *draggablePinView = [self.mapView dequeueReusableAnnotationViewWithIdentifier:kPinAnnotationIdentifier];
	
	if (draggablePinView) {
		draggablePinView.annotation = annotation;
	} else {
		// Use class method to create DDAnnotationView (on iOS 3) or built-in draggble MKPinAnnotationView (on iOS 4).
		draggablePinView = [DDAnnotationView annotationViewWithAnnotation:annotation reuseIdentifier:kPinAnnotationIdentifier mapView:self.mapView];
				
		if ([draggablePinView isKindOfClass:[DDAnnotationView class]]) {
			// draggablePinView is DDAnnotationView on iOS 3.
		} else {
			// draggablePinView instance will be built-in draggable MKPinAnnotationView when running on iOS 4.
		}
	}		
	
	return draggablePinView;
}

-(void)addNavigationItems
{
    {
        UIButton* editProfile =[[[UIButton alloc] initWithFrame:CGRectMake(270, 3, 23, 23)] autorelease];
        
        UIImage*buttonImage = [[UIImage imageNamed:@"information.png"] stretchableImageWithLeftCapWidth:CAP_WIDTH topCapHeight:0.0 ];
        [editProfile setBackgroundImage:buttonImage forState:UIControlStateNormal];
        editProfile.titleLabel.font = [UIFont boldSystemFontOfSize:[UIFont smallSystemFontSize]];
        editProfile.titleLabel.textColor = [UIColor whiteColor];
        editProfile.titleLabel.shadowOffset = CGSizeMake(0,-1);
        editProfile.titleLabel.shadowColor = [UIColor darkGrayColor];
        editProfile.backgroundColor = [UIColor clearColor];
        [editProfile
         setTitle:@"" forState:UIControlStateNormal];
        editProfile.layer.cornerRadius = 5.0;
        [editProfile addTarget:self action:@selector(leftButtonAction) forControlEvents:UIControlEventTouchUpInside];
        
        UIBarButtonItem*editProfileBarItem = [[UIBarButtonItem alloc]initWithCustomView:editProfile ];
        
        
        self.navigationController.navigationItem.leftBarButtonItem = editProfileBarItem;
        self.navigationItem.leftBarButtonItem = editProfileBarItem;
        
    }
    
    {
        UIButton* editProfile =[[[UIButton alloc] initWithFrame:CGRectMake(270, 3, 23, 23)] autorelease];
        
        UIImage*buttonImage = [[UIImage imageNamed:@"refresh.png"] stretchableImageWithLeftCapWidth:CAP_WIDTH topCapHeight:0.0 ];
        [editProfile setBackgroundImage:buttonImage forState:UIControlStateNormal];
        editProfile.titleLabel.font = [UIFont boldSystemFontOfSize:[UIFont smallSystemFontSize]];
        editProfile.titleLabel.textColor = [UIColor whiteColor];
        editProfile.titleLabel.shadowOffset = CGSizeMake(0,-1);
        editProfile.titleLabel.shadowColor = [UIColor darkGrayColor];
        editProfile.backgroundColor = [UIColor clearColor];
        [editProfile
         setTitle:@"" forState:UIControlStateNormal];
        editProfile.layer.cornerRadius = 5.0;
        [editProfile addTarget:self action:@selector(rightButtonAction) forControlEvents:UIControlEventTouchUpInside];
        
        UIBarButtonItem*editProfileBarItem = [[UIBarButtonItem alloc]initWithCustomView:editProfile ];
        
        self.navigationController.navigationItem.rightBarButtonItem = editProfileBarItem;
        self.navigationItem.rightBarButtonItem = editProfileBarItem;
    }
    
}


-(void)singleTapScrollViewAction:(id*)sender
{
    NSLog(@"singleTapScrollViewAction  ..");
    UITapGestureRecognizer* localSinglerTap = (UITapGestureRecognizer*)sender;
    CGPoint point =[localSinglerTap locationInView:self.bottomBackgroundboard];
    if (map_view_state == 0 &&point.y > 20 ) {
        //Small map
        [self singleTapMapView];
    }
    else if (map_view_state == 1)
    {
        [self singleTapMapView];
    }
    
}
static int map_view_state = 0;
-(void)singleTapMapView
{
    NSLog(@"huangzf.....");
    if (map_view_state == 0) {
        
        mapView.userInteractionEnabled = YES;
        ////////full screen
        [self.navigationController setNavigationBarHidden:YES animated:YES];
        map_view_state = 1;
        [UIView animateWithDuration:0.2 animations:^{
            mapViewOriginRect = mapView.frame;
            [mapView setFrame:CGRectMake(0, 0, 320, 480)];
            
        } completion:^(BOOL finished) {
            [UIView animateWithDuration:0.5 animations:^{
                //[view setAlpha:0];
            }];
        }];
        
    }else{
        //////Small screen
        [self.navigationController setNavigationBarHidden:NO animated:YES];
        //should reset the center of the mapview.
         [self.mapView setCenterCoordinate:[self.mapView.userLocation coordinate] zoomLevel:MAP_DEFAULT_ZOOM_LEVEL animated:NO];
        mapView.userInteractionEnabled = NO;
        map_view_state = 0;
        [UIView animateWithDuration:0.2 animations:^{
            [mapView setFrame:mapViewOriginRect];
            
        } completion:^(BOOL finished) {
            [UIView animateWithDuration:0.5 animations:^{
                //[view setAlpha:0];
            }];
        }];
        
    }
    
}
-(void)handleOverOrderInformationCheck:(NSDictionary*)dict
{
    
}
-(void)handleOverOrderReturn:(NSMutableDictionary*)dict
{
    
}
-(void)doSuccess:(NSDictionary *)dict
{
     [NSThread sleepForTimeInterval:1.0];
   
    NSLog(@"this is do success %@", dict);
    NSNumber* stateString = [dict valueForKey:@"state"];
    if (stateString!=nil) {
        /////if there is state string return , this is a request for order information check.
        [HUD show:NO];
        [HUD setHidden:YES];
        [self.myScrollView setHidden:NO];
        [UIView animateWithDuration:0.5 animations:^{
            CATransition *animation = [CATransition animation];
            [animation setType:@"suckEffect"];
            [self.myScrollView.layer addAnimation:animation forKey:@"Reveal"];
            //[self.myScrollView setHidden:YES];
        } completion:^(BOOL finished) {
        [UIView animateWithDuration:0.5 animations:^{
            //[view setAlpha:0];
            //[self.myScrollView setHidden:YES];
            //[self.myScrollView setFrame:CGRectMake(0, 0, 320, 480)];
        }];
    }];
        NSString* order_idString = [dict valueForKey:@"order_id"];
        orderStateHintLabel.text = [@"为您查询到了最近空置出租车 单号为:" stringByAppendingFormat:@"%@",order_idString];
        NSString* temp1 = [@"" stringByAppendingFormat:@"+86 13%d **** 1%d3%d", rand()%10, rand()%10,rand()%10];
        [self.phoneNumberButton1 setTitle:temp1 forState:UIControlStateNormal];
        
        NSString* temp2 = [@"" stringByAppendingFormat:@"+86 13%d **** 1%d3%d", rand()%10, rand()%10,rand()%10];
        [self.phoneNumberButton2 setTitle:temp2 forState:UIControlStateNormal];

        
    }
    else{
        //////otherwise this is a order id return request.
        
        
        NSString* order_idString = [dict valueForKey:@"order_id"];
        HttpClient* httpclient = [[HttpClient alloc] initWithDelegate:self];
        [httpclient getOrderInformation:order_idString];
        
        orderStateHintLabel.text = [@"系统已经接受您的查询请求 单号为" stringByAppendingFormat:@"%@", order_idString];
        
    }
    
}
-(void)doFail:(NSString *)msg
{
    
    [HUD show:NO];
    [HUD setHidden:YES];
    NSLog(@"this is do fail: %@", msg);
    
    
    //even fail , we should reload.
   
}

-(void)doNetWorkFail
{
    [HUD show:NO];
    [HUD setHidden:YES];
    NSLog(@"This is NetWork fail");
}

- (void)hudWasHidden {
    // Remove HUD from screen when the HUD was hidded
    [HUD removeFromSuperview];
    [HUD release];
}
- (IBAction)StartPhoneCallbutton1:(id)sender {
    [self startPhoneCall];
}

- (IBAction)startPhoneCallButton2:(id)sender {
    [self startPhoneCall];
}
@end
