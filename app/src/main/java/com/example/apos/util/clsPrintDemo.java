package com.example.apos.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.activity.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;


public class clsPrintDemo extends AppCompatActivity {

	TextView myLabel;
	public static OutputStream mmOutputStream;
	public static InputStream mmInputStream;
	public static BluetoothSocket socket;

	// android built in classes for bluetooth operations
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket mmSocket;
	BluetoothDevice mmDevice;
	Thread workerThread;

	byte[] readBuffer;
	int readBufferPosition;
	public static volatile boolean stopWorker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clsbluetoothprintdemo);
	}


	// public void funPrintViaBluetoothPrinter(String printData,BluetoothAdapter mBluetoothAdapter)
	public void funPrintViaBluetoothPrinter(BluetoothAdapter mBluetoothAdapter) {

		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			if (mBluetoothAdapter == null) {
				Toast.makeText(clsPrintDemo.this, "Bluetooth Adapter not enable", Toast.LENGTH_LONG).show();
			}
			if (!mBluetoothAdapter.isEnabled()) {
				Toast.makeText(clsPrintDemo.this, "Bluetooth printer not enable", Toast.LENGTH_LONG).show();
				Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBluetooth, 0);
			}

			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {

					// RPP300 is the name of the bluetooth printer device
					// we got this name from the list of paired devices
					if (device.getName().equals("RPP-02")) {
						mmDevice = device;
						try {
							openBT(mmDevice, mBluetoothAdapter);

						} catch (IOException ex) {
							ex.printStackTrace();
						}

					} else if (device.getName().contains("TM-P20")) {
						mmDevice = device;
						try {
							openBT(mmDevice, mBluetoothAdapter);

						} catch (IOException ex) {
							ex.printStackTrace();
						}

					}
				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// this will find a bluetooth printer device
	public void findBT() {

		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

			if (mBluetoothAdapter == null) {
				myLabel.setText("No bluetooth adapter available");
			}

			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBluetooth, 0);
			}

			Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

			if (pairedDevices.size() > 0) {
				for (BluetoothDevice device : pairedDevices) {

					// RPP300 is the name of the bluetooth printer device
					// we got this name from the list of paired devices
					if (device.getName().equals("RPP-02")) {
						mmDevice = device;

						break;
					}
				}
			}

			myLabel.setText("Bluetooth device found.");
			Toast.makeText(clsPrintDemo.this, "Bluetooth device found", Toast.LENGTH_LONG).show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// tries to open a connection to the bluetooth printer device
	public void openBT(BluetoothDevice device, BluetoothAdapter mBluetoothAdapter) throws IOException {
		try {


			UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
			System.out.println("uuid=" + uuid);

			socket = device.createInsecureRfcommSocketToServiceRecord(uuid);
			Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class});
			socket = (BluetoothSocket) m.invoke(device, 1);
			mBluetoothAdapter.cancelDiscovery();
			socket.connect();
			mmOutputStream = socket.getOutputStream();
			mmInputStream = socket.getInputStream();

			beginListenForData(mmOutputStream, mmInputStream);
			// sendData(printData,mmOutputStream,mmInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// this will send text data to be printed by the bluetooth printer
	public static void sendData(String printData, OutputStream mmOutputStream, InputStream mmInputStream) throws IOException {
		try {

			// the text typed by the user
			String msg = printData;
			msg += "\n";
			mmOutputStream.write(msg.getBytes());
			//closeBT(mmOutputStream,mmInputStream);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// close the connection to bluetooth printer.
	public static void closeBT(OutputStream mmOutputStream, InputStream mmInputStream, BluetoothSocket mSocket) throws IOException {
		try {
			stopWorker = true;
			mmOutputStream.close();
			mmInputStream.close();
			mSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
 * after opening a connection to bluetooth printer device,
 * we have to listen and check if a data were sent to be printed.
 */
	public void beginListenForData(OutputStream mmOutputStream, final InputStream mmInputStream) {
		try {
			final Handler handler = new Handler();

			// this is the ASCII code for a newline character
			final byte delimiter = 10;

			stopWorker = false;
			readBufferPosition = 0;
			readBuffer = new byte[1024];

			workerThread = new Thread(new Runnable() {
				public void run() {

					while (!Thread.currentThread().isInterrupted() && !stopWorker) {

						try {

							int bytesAvailable = mmInputStream.available();

							if (bytesAvailable > 0) {

								byte[] packetBytes = new byte[bytesAvailable];
								mmInputStream.read(packetBytes);

								for (int i = 0; i < bytesAvailable; i++) {

									byte b = packetBytes[i];
									if (b == delimiter) {

										byte[] encodedBytes = new byte[readBufferPosition];
										System.arraycopy(
												readBuffer, 0,
												encodedBytes, 0,
												encodedBytes.length
										);

										// specify US-ASCII encoding
										final String data = new String(encodedBytes, "US-ASCII");
										readBufferPosition = 0;

										// tell the user data were sent to bluetooth printer device
										handler.post(new Runnable() {
											public void run() {
												myLabel.setText(data);
											}
										});

									} else {
										readBuffer[readBufferPosition++] = b;
									}
								}
							}

						} catch (IOException ex) {
							stopWorker = true;
						}

					}
				}
			});

			workerThread.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
