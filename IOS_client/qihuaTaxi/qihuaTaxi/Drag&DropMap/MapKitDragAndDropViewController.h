//
//  MapKitDragAndDropViewController.h
//  MapKitDragAndDrop
//
//  Created by digdog on 11/1/10.
//  Copyright 2010 Ching-Lan 'digdog' HUANG. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "UIGlossyButton.h"
#import "HttpClient.h"
#import "FXLabel.h"
#import "MBProgressHUD.h"
@interface MapKitDragAndDropViewController : UIViewController <MKMapViewDelegate, HttpClientDelegate, CLLocationManagerDelegate,MBProgressHUDDelegate> {
	MKMapView *mapView;
    int needSendingLocationMap;
    CGRect mapViewOriginRect;
    CLLocationManager *locationManager;
    ///////////////////////////////////
    NSString* LatString;
    NSString* LonString;
    MBProgressHUD *HUD;
    
    ///////////////////////////////////
    UITapGestureRecognizer *singleTap;
}

@property(nonatomic,retain) NSString* LatString;
@property(nonatomic,retain) NSString* LonString;
@property(nonatomic,assign)    int needSendingLocationMap;
@property (nonatomic, retain) IBOutlet MKMapView *mapView;
@property (retain, nonatomic) IBOutlet UIScrollView *myScrollView;

@property (retain, nonatomic) IBOutlet UILabel *orderStateHintLabel;

@property (retain, nonatomic) IBOutlet UIImageView *divideImage1;

@property (retain, nonatomic) IBOutlet UIImageView *divideImage2;

@property (retain, nonatomic) IBOutlet UIImageView *divideImage3;
@property (retain, nonatomic) IBOutlet UIButton *phoneNumberButton1;

@property (retain, nonatomic) IBOutlet UIButton *phoneNumberButton2;

@property (retain, nonatomic) IBOutlet UILabel *taxiInfoLabel1;
@property (retain, nonatomic) IBOutlet UILabel *taxiInfoLabel2;
@property (retain, nonatomic) IBOutlet UILabel *taxiInfoLabel3;
@property (retain, nonatomic) IBOutlet UILabel *taxiInfoLabel4;
@property (retain, nonatomic) IBOutlet UILabel *taxiInfoLabel5;


@property (retain, nonatomic) IBOutlet FXLabel *taxiInfoLabelvalue1;
@property (retain, nonatomic) IBOutlet FXLabel *taxiInfoLabelvalue2;
@property (retain, nonatomic) IBOutlet FXLabel *taxiInfoLabelvalue3;
@property (retain, nonatomic) IBOutlet FXLabel *taxiInfoLabelvalue4;
@property (retain, nonatomic) IBOutlet FXLabel *taxiInfoLabelvalue5;

@property (retain, nonatomic) IBOutlet UIView *bottomBackgroundboard;

@property (retain, nonatomic) IBOutlet UILabel *bottomStateLabel;
@property (retain, nonatomic) IBOutlet UILabel *hintTitle;
@property (retain, nonatomic) IBOutlet UILabel *hintContent;

- (IBAction)StartPhoneCallbutton1:(id)sender;
- (IBAction)startPhoneCallButton2:(id)sender;



@end

