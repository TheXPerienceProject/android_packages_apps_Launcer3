package com.google.android.libraries.launcherclient;

import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import android.view.WindowManager;

public interface ILauncherOverlay extends IInterface
{
    void closeOverlay(final int p0) throws RemoteException;
    
    void endScroll() throws RemoteException;
    
    String getVoiceSearchLanguage() throws RemoteException;
    
    boolean hasOverlayContent() throws RemoteException;
    
    boolean isVoiceDetectionRunning() throws RemoteException;
    
    void onPause();
    
    void onResume();
    
    void onScroll(final float p0) throws RemoteException;
    
    void openOverlay(final int p0) throws RemoteException;
    
    void requestVoiceDetection(final boolean p0);
    
    void setActivityState(final int p0) throws RemoteException;
    
    void startScroll() throws RemoteException;
    
    boolean startSearch(final byte[] p0, final Bundle p1) throws RemoteException;
    
    void windowAttached(final WindowManager.LayoutParams p0, final ILauncherOverlayCallback p1, final int p2) throws RemoteException;
    
    void windowAttached2(final Bundle p0, final ILauncherOverlayCallback p1) throws RemoteException;
    
    void windowDetached(final boolean p0) throws RemoteException;
}
