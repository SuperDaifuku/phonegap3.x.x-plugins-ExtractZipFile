//-----------------------------------
//  Modified: punger 08.08.2013
//  -> work in own thread
//  -> corrected filepaths
//  -> delete zip-file after decompression
//  -> updated to work with phonegap 3.0.0
//-----------------------------------
// 
//  ExtractZipFilePlugin.m
//
//  Created by Shaun Rowe on 10/05/2012.
//  Copyright (c) 2012 Pobl Creative Cyf. All rights reserved.
//

#import "ExtractZipFilePlugin.h"

@implementation ExtractZipFilePlugin

@synthesize callbackID;

- (void)extract:(CDVInvokedUrlCommand*)command
{
    NSArray *arguments = command.arguments;
    self.callbackID = command.callbackId;

    [self.commandDelegate runInBackground:^{
        
        NSString *documentDir = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
        
        NSString *file = [arguments objectAtIndex:0];
        file = [documentDir stringByAppendingString:file];
        
        NSString *destination = [arguments objectAtIndex:1];
        destination  = [documentDir stringByAppendingString:destination];
        
        CDVPluginResult *result;
        if([SSZipArchive unzipFileAtPath:file toDestination:destination delegate:nil]) {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[destination stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            
            [[NSFileManager defaultManager] removeItemAtPath:file error:nil];
            [self.commandDelegate sendPluginResult:result callbackId:callbackID];

        } else {
            result = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:[@"Could not extract archive" stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
            
            NSLog(@"%@", @"Could not extract zip archive");
            
            [[NSFileManager defaultManager] removeItemAtPath:file error:nil];
            [self.commandDelegate sendPluginResult:result callbackId:callbackID];
        }
    }];
}

@end