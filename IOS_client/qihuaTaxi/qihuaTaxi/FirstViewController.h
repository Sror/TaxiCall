//
//  FirstViewController.h
//  qihuaTaxi
//
//  Created by Alex on 12/3/12.
//  Copyright (c) 2012 Alex. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "DDAnnotation.h"
#import "DDAnnotationView.h"
#import "MKMapView+ZoomLevel.h"
#import "DataSingleton.h"
#import <QuartzCore/QuartzCore.h>

@interface FirstViewController : UIViewController<MKMapViewDelegate,CLLocationManagerDelegate>
{
    MKMapView *mapView;
}
@property (weak, nonatomic) IBOutlet UIScrollView *myScrollView;

@end
