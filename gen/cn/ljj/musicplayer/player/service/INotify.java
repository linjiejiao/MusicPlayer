/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /media/D/Pro Code/git/MusicPlayer/src/cn/ljj/musicplayer/player/service/INotify.aidl
 */
package cn.ljj.musicplayer.player.service;
public interface INotify extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements cn.ljj.musicplayer.player.service.INotify
{
private static final java.lang.String DESCRIPTOR = "cn.ljj.musicplayer.player.service.INotify";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an cn.ljj.musicplayer.player.service.INotify interface,
 * generating a proxy if needed.
 */
public static cn.ljj.musicplayer.player.service.INotify asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof cn.ljj.musicplayer.player.service.INotify))) {
return ((cn.ljj.musicplayer.player.service.INotify)iin);
}
return new cn.ljj.musicplayer.player.service.INotify.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_setCallback:
{
data.enforceInterface(DESCRIPTOR);
cn.ljj.musicplayer.player.service.INotify _arg0;
_arg0 = cn.ljj.musicplayer.player.service.INotify.Stub.asInterface(data.readStrongBinder());
this.setCallback(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_onNotify:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
long _arg2;
_arg2 = data.readLong();
java.lang.String _arg3;
_arg3 = data.readString();
cn.ljj.musicplayer.data.MusicInfo _arg4;
if ((0!=data.readInt())) {
_arg4 = cn.ljj.musicplayer.data.MusicInfo.CREATOR.createFromParcel(data);
}
else {
_arg4 = null;
}
int _result = this.onNotify(_arg0, _arg1, _arg2, _arg3, _arg4);
reply.writeNoException();
reply.writeInt(_result);
if ((_arg4!=null)) {
reply.writeInt(1);
_arg4.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements cn.ljj.musicplayer.player.service.INotify
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void setCallback(cn.ljj.musicplayer.player.service.INotify callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setCallback, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public int onNotify(int cmd, int intValue, long longValue, java.lang.String str, cn.ljj.musicplayer.data.MusicInfo music) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(cmd);
_data.writeInt(intValue);
_data.writeLong(longValue);
_data.writeString(str);
if ((music!=null)) {
_data.writeInt(1);
music.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onNotify, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
if ((0!=_reply.readInt())) {
music.readFromParcel(_reply);
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_setCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onNotify = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void setCallback(cn.ljj.musicplayer.player.service.INotify callback) throws android.os.RemoteException;
public int onNotify(int cmd, int intValue, long longValue, java.lang.String str, cn.ljj.musicplayer.data.MusicInfo music) throws android.os.RemoteException;
}
