//
//  FirstViewController.m
//  qihuaTaxi
//
//  Created by Alex on 12/3/12.
//  Copyright (c) 2012 Alex. All rights reserved.
//

#import "FirstViewController.h"
@interface FirstViewController ()
- (void)coordinateChanged_:(NSNotification *)notification;

@end
#define MAP_DEFAULT_ZOOM_LEVEL 12

@implementation FirstViewController
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        self.title = NSLocalizedString(@"First", @"First");
        self.tabBarItem.image = [UIImage imageNamed:@"first"];
    }
    return self;
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


- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    ///default location.
    [self.myScrollView setContentSize:CGSizeMake(320, 500)];
    
	CLLocationCoordinate2D theCoordinate;
	theCoordinate.latitude = 39.912534;
    theCoordinate.longitude = 116.454091;
	
	DDAnnotation *annotation = [[DDAnnotation alloc] initWithCoordinate:theCoordinate addressDictionary:nil] ;
	annotation.title = @"按住图钉，拖动编注位置";
	annotation.subtitle = [NSString	stringWithFormat:@"%f %f", annotation.coordinate.latitude, annotation.coordinate.longitude];
	mapView = [[MKMapView alloc] initWithFrame:CGRectMake(0, 200, 320, 200)];
    mapView.delegate = self;
	[mapView addAnnotation:annotation];
    
    mapView.showsUserLocation = YES;
    double lat = [mapView.userLocation coordinate].latitude;
    double lon = [mapView.userLocation coordinate].longitude;
    if (ABS(lat - (-180.0)) < 0.005 && ABS(lon -(-180.0) < 0.005 )) {
        [mapView setCenterCoordinate:theCoordinate zoomLevel:MAP_DEFAULT_ZOOM_LEVEL animated:NO];
    }
    else {
        //theCoordinate
        [mapView setCenterCoordinate:[mapView.userLocation coordinate] zoomLevel:MAP_DEFAULT_ZOOM_LEVEL animated:NO];
        
    }
    [self.myScrollView addSubview:mapView];
    
    
    
    CLLocationManager *locationManager = [[CLLocationManager alloc] init];//创建位置管理器
    
    locationManager.delegate=self;//设置代理
    
    locationManager.desiredAccuracy=kCLLocationAccuracyBest;//指定需要的精度级别
    locationManager.distanceFilter=100.0f;//设置距离筛选器
    [locationManager startUpdatingLocation];//启动位置管理器
    
    //设置点击计入下一级
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(singleTapMapView)] ;
    [mapView addGestureRecognizer:singleTap];
    mapView.userInteractionEnabled = YES;
}
-(void)singleTapMapView
{
    NSLog(@"huangzf.....");
}
- (void)locationManager:(CLLocationManager *)manager
    didUpdateToLocation:(CLLocation *)newLocation
           fromLocation:(CLLocation *)oldLocation
{
   // NSString *latitudeString=[[NSString alloc] initWithFormat:@"%g",newLocation.coordinate.latitude];
    
   // NSString *longitudeString=[[NSString alloc] initWithFormat:@"%g",newLocation.coordinate.longitude];
    
    
    [mapView setCenterCoordinate:[mapView.userLocation coordinate] zoomLevel:MAP_DEFAULT_ZOOM_LEVEL animated:NO];
    
    
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
   
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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

- (MKAnnotationView *)mapView:(MKMapView *)localmapView viewForAnnotation:(id <MKAnnotation>)annotation {
	
    if ([annotation isKindOfClass:[MKUserLocation class]]) {
        return nil;
	}
	
	static NSString * const kPinAnnotationIdentifier = @"PinIdentifier";
	MKAnnotationView *draggablePinView = [localmapView dequeueReusableAnnotationViewWithIdentifier:kPinAnnotationIdentifier];
	
	if (draggablePinView) {
		draggablePinView.annotation = annotation;
	} else {
		// Use class method to create DDAnnotationView (on iOS 3) or built-in draggble MKPinAnnotationView (on iOS 4).
		draggablePinView = [DDAnnotationView annotationViewWithAnnotation:annotation reuseIdentifier:kPinAnnotationIdentifier mapView:localmapView];
        
		if ([draggablePinView isKindOfClass:[DDAnnotationView class]]) {
			// draggablePinView is DDAnnotationView on iOS 3.
		} else {
			// draggablePinView instance will be built-in draggable MKPinAnnotationView when running on iOS 4.
		}
	}
	
	return draggablePinView;
}

- (void)viewDidUnload {
    [self setMyScrollView:nil];
    [super viewDidUnload];
}
@end
