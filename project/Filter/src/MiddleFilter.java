import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/******************************************************************************************************************
* File:MiddleFilter.java
* Project: Lab 1
* Copyright:
*   Copyright (c) 2020 University of California, Irvine
*   Copyright (c) 2003 Carnegie Mellon University
* Versions:
*   1.1 January 2020 - Revision for SWE 264P: Distributed Software Architecture, Winter 2020, UC Irvine.
*   1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
* Parameters: None
* Internal Methods: None
******************************************************************************************************************/

public class MiddleFilter extends FilterFramework
{
	private List<List<String>> data = new ArrayList<>();
	private double prevAltitude = -1.0;
	private double prevTwoAltitude = -1.0;

	public void run()
    {
		// Next we write a message to the terminal to let the world know we are alive...
		System.out.print( "\n" + this.getName() + "::Middle Reading ");

		filter();
   }

	private void filter() {
		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

		int MeasurementLength = 8;        // This is the length of all measurements (including time) in bytes
		int IdLength = 4;                 // This is the length of IDs in the byte stream
		byte databyte = 0;                // This is the data byte read from the stream
		int bytesread = 0;                // This is the number of bytes read from the stream
		long measurement;                // This is the word used to store all measurements - conversions are
		int byteswritten = 0;				// Number of bytes written to the stream.
		// illustrated.
		int id;                            // This is the measurement id
		int i;                            // This is a loop counter
		double velocity = 0.0;
		double altitude = 0.0;
		double pressure = 0.0;
		double temperature = 0.0;

		boolean isWildJump = false;

		while (true) {
			try {
				id = 0;
				for (i = 0; i < IdLength; i++) {
					databyte = ReadFilterInputPort();    // This is where we read the byte from the stream...
					id = id | (databyte & 0xFF);        // We append the byte on to ID...
					if (i != IdLength - 1)                // If this is not the last byte, then slide the
					{                                    // previously appended byte to the left by one byte
						id = id << 8;                    // to make room for the next byte we append to the ID
					}
					bytesread++;                        // Increment the byte count
					WriteFilterOutputPort(databyte);
					byteswritten++;
				}

				measurement = 0;
				byte[] originalData = new byte[8];
				for (i = 0; i < MeasurementLength; i++) {
					databyte = ReadFilterInputPort();
					measurement = measurement | (databyte & 0xFF);    // We append the byte on to measurement...
					if (i != MeasurementLength - 1)                    // If this is not the last byte, then slide the
					{                                                // previously appended byte to the left by one
						measurement = measurement << 8;                // to make room for the next byte we append to
					}
					bytesread++;                                    // Increment the byte count
					originalData[i] = databyte;
					byteswritten++;
				}

				if (id == 0) {
					TimeStamp.setTimeInMillis(measurement);
					writeOutputStream(originalData);
				} else if (id == 1) {
					velocity = Double.longBitsToDouble(measurement);
					writeOutputStream(originalData);
				} else if (id == 2) {
					altitude = Double.longBitsToDouble(measurement);
					isWildJump = prevAltitude != -1.0 && Math.abs(altitude - prevAltitude) > 100.0;

					if (isWildJump) {
						if (prevTwoAltitude == -1.0) {
							altitude = prevAltitude;
						} else {
							altitude = -(prevAltitude + prevTwoAltitude) / 2;
						}
						writeOutputStream(double2Bytes(altitude));
					} else {
						writeOutputStream(originalData);
					}

					prevTwoAltitude = prevAltitude;
					prevAltitude = Double.longBitsToDouble(measurement);
				} else if (id == 3) {
					pressure = Double.longBitsToDouble(measurement);
					writeOutputStream(originalData);
				} else if (id == 4) {
					writeOutputStream(originalData);
					temperature = Double.longBitsToDouble(measurement);
					if (isWildJump) {
						data.add(makeCsvData(TimeStampFormat.format(TimeStamp.getTime()), velocity, altitude, pressure, temperature));
					}
				}
			} catch (EndOfStreamException e) {
				ClosePorts();
				System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread);
				break;
			}
		}

		CsvUtils.print(data, "./WildPoints.csv");
	}

	private void writeOutputStream(byte[] data) {
		for (byte datum : data) {
			WriteFilterOutputPort(datum);
		}
	}

	public static byte[] double2Bytes(double d) {
		long data = Double.doubleToRawLongBits(d);
		return new byte[]{
				(byte) ((data >> 56) & 0xff),
				(byte) ((data >> 48) & 0xff),
				(byte) ((data >> 40) & 0xff),
				(byte) ((data >> 32) & 0xff),
				(byte) ((data >> 24) & 0xff),
				(byte) ((data >> 16) & 0xff),
				(byte) ((data >> 8) & 0xff),
				(byte) ((data >> 0) & 0xff),
		};
	}

	public static byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(0, x);
		return buffer.array();
	}

	public static byte[] intToByteArray(int a) {
		return new byte[] {
				(byte) ((a >> 24) & 0xFF),
				(byte) ((a >> 16) & 0xFF),
				(byte) ((a >> 8) & 0xFF),
				(byte) (a & 0xFF)
		};
	}
}