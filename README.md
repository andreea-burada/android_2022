## Android Notes

### Parcelable interface

-> implies serialization and de-serialization
-> ser. and de-ser should be written in the *SAME* order (if you ser. var1, var2, var3 then you have to de-ser. them in the same order 1, 2, 3 ...)

#### Special Cases

-> for serializing booleans:
```
destination.writeInt(booleanVar == null ? 0 : (booleanVar == true ? 1 : 0));
```
-> for serializing variables that could be null
	-> write a dummy Byte (0 or 1) so when you de-serialize you know if you had null or a value
```
if (variable == null) {
    destination.writeByte((byte) 0);
} 
else {
    destination.writeByte((byte) 1);
    destination.writeInt(variable);
}
```

### Form Activity

-> void method in which you set all components from the ENTITY class

#### *spinner*
-> setting spinner with array of strings from XML
```
<array name="whatever">
	<item>Item1</item>
	<item>Item2</item>
	...
</array>
```
```
android:entries="@array/whatever"
```
-> setting spinner values from an enum class
```
public enum Whatever { Item1, Item2, ... }
```
```
spinner.setAdapter(new ArrayAdapter<Whatever>(this, android.R.layout.simple_spinner_item, Whatever.values()));
```

#### *date picker*
```
// create calendar instance and get today date (year, month, day)
Calendar calendar = Calendar.getInstance();
int year = calendar.get(Calendar.YEAR);
int month = calendar.get(Calendar.MONTH);
int day = calendar.get(Calendar.DAY_OF_MONTH);

// create date picker dialogue window
DatePickerDialogue datePickerDialogue = new DatePickerDialog (context: FormActivity.this, ... auto-generated ... {
	@Override
	public void onDateSet( ... auto-generated ... ) {
		// get selected date year, month, date
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);

		// get the date in Date format
		Date variable2save = calendar.getTime();

		// save variable
		entity.setDate(variable2save);

		// set the text on the button
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern: "MMM dd yyyy");
		button.setText(dateFormat.format(variable2save));
	}

}, year, month, day);

// show dialogue
datePickerDialogue.show();
```