## Android Notes

### ENTITY class
-> class with which we work, think Student, Movie etc. \
-> *implements* ***Parcelable*** -> auto-generate methods for interface \
&ensp;&ensp;&ensp;&ensp;-> `public int describeContents()` \
&ensp;&ensp;&ensp;&ensp;-> `public void writeToParcel(Parcel parcel/destination, int i)`	-> this is where we **SERIALIZE** \
&ensp;&ensp;&ensp;&ensp;-> `creator` field will also be auto-generated \
&ensp;&ensp;&ensp;&ensp;-> `protected Entity(Parcel in)`	-> this is where we **DE-SERIALIZE**

#### Parcelable interface

-> implies serialization and de-serialization \
-> ser. and de-ser should be written in the **SAME** order (if you ser. var1, var2, var3 then you have to de-ser. them in the same order 1, 2, 3 ...)

##### Special Cases

-> for *serializing booleans*:
```
destination.writeInt(booleanVar == null ? 0 : (booleanVar == true ? 1 : 0));
```
-> for *serializing variables that could be null* \
&ensp;&ensp;&ensp;&ensp;-> write a dummy Byte (0 or 1) so when you de-serialize you know if you had null or a value
```
if (variable == null) {
    destination.writeByte((byte) 0);
} 
else {
    destination.writeByte((byte) 1);
    destination.writeInt(variable);
}
```
---
### Form Activity

-> attribute of `class Entity` which we will edit with setters to receive the data from the form \
-> in `onCreate` we initialize attribute entity -> `entity = new Entity();` \
-> void method `initializeComponents` in which you set all components from the ENTITY class

#### *spinner*
-> setting spinner with array of strings from XML
```
<array name="whatever">
	<item>Item1</item>
	<item>Item2</item>
	...
</array>
```
-> in the Form Activity XML under spinner write
```
android:entries="@array/whatever"
```
-> setting spinner values from an enum class
```
public enum Whatever { Item1, Item2, ... }
```
-> in the method where components gets initialized write
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

#### *save button*
-> set any attributes that were not set previously (eg: title for Movie) \
-> launch second activity which displays the collection (ArrayList) items
```
Intent intent = new Intent(packageContext: FormActivity.this, ListActivity.class);
// add the entity with data to the intent
intent.putExtra(name: "entity", entity);
startActivity(intent);
```
---
### List Activity
-> *attributes* \
&ensp;&ensp;&ensp;&ensp;- `static List<Entity> entityList = new ArrayList<>();` \
&ensp;&ensp;&ensp;&ensp;- `RecyclerView recyclerView;` \
&ensp;&ensp;&ensp;&ensp;- `ExecutorService executorService;` \
\
-> in the `onCreate` method we need to get the entity we received through the intent, and to set a custom adapter to the recycler view
```
Intent intent = getIntent();

// get the entity from the intent
Bundle extras = intent.getExtras();
Entity entityReceived = extras.getParcelable(key: "entity");

// add entity to the list
entityList.add(entity);

// set recycler view
recyclerView = findViewById(R.id.recyclerViewId);

// set executor service threads
executorService = Executors.newFixedThreadPool(nThreads: 4);

// create custom adapter
EntityAdapter entityAdapter = new EntityAdapter(context: this, entityList, executorService);

// set adapter to recycler
recyclerView.setAdapter(entityAdapter);
```
-> in `onPause` shut down the executor
```
@Override
protected void onPause() {
    super.onPause();
    executorService.shutdown();
}
```
---
### Custom Adapter - Entity Adapter
-> `extends RecyclerView.Adapter<EntityAdapter.EntityHolder` -> see [**Entity Holder**](### Entity Holder) below for more info \
-> attributes \
&ensp;&ensp;&ensp;&ensp;- `List<Entity> entityList` \
&ensp;&ensp;&ensp;&ensp;- `Context entityContext` \
&ensp;&ensp;&ensp;&ensp;- `ExecutorService executorService` \
&ensp;&ensp;&ensp;&ensp;- `Handler uiHandler` // for networking \
\
-> default constructor will have the `Context, List<Entity>, ExecutorService` parameters list \
\
-> `onCreateViewHolder` method
```
// inflate layout
LayoutInflater inflater = LayoutInflater.from(entityContext);
View itemView = inflater.inflate(R.layout.entity_item, parent, attachToRoot: false);

// create entity holder
EntityHolder viewHolder = new EntityHolder(itemView);

return viewHolder;
```
-> `onBindViewHolder` method \
&ensp;&ensp;&ensp;&ensp;-> it contains a `holder` which is of EntityHolder type \
&ensp;&ensp;&ensp;&ensp;-> here you set all components values and event listeners if needed \
&ensp;&ensp;&ensp;&ensp;-> this is also where you get resources or anything like that \
&ensp;&ensp;&ensp;&ensp;-> to get current Entity `entityList.get(position)` \
\
-> `getItemCount()` method
```
return entityList.size()
```
---
### Entity Holder
-> `extends RecyclerView.ViewHolder` \
-> attributes are the components from `entity_item.xml` layout \
-> attribute `view` \
\
-> in constructor which takes `View` parameter initialize all components based on the `entity_item.xml` names

---
### Getting Image from the Internet
-> in `strings.xml`
```
<string name="NameToFindResource">[url]</string>
...
```
-> get resourceId of url
```
int resourceId = entityContext.getResources().getIdentifier(entity.getAttribute(), defType: "string", entityContext.getPackageName()); // .getAttribute() reffers to any method that could return "NameToFindResource"
								 // eg. .getTitle() for Movie

if (resourceId != 0) {
	// download the image
    uiHandler = new Handler(getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
        	// code that we set as the "successful" code
            int wantedBitmapCode = 1122;
            if (message.what == wantedBitmapCode)
            {
            	// set image to the component that we want to display it
                holder.imageViewId.setImageBitmap((Bitmap) message.obj);
            }
            // return true regardless of outcome
            return true;
        }
    });
    // through the executor service submit a new instance of class DownloadImageTask
    // entityContext.getString(resourceId) - url from .xml
    executorService.submit(new DownloadImageTask(entityContext.getString(resourceId), uiHandler));
}
```
-> see **Download Image Task Class**

---
### Download Image Task Class
-> `extends Thread` \
-> `implements Runnable` \
-> attributes \
&ensp;&ensp;&ensp;&ensp;- `String imageUrl` \
&ensp;&ensp;&ensp;&ensp;- `Handler handler` -> uiHandler from [**EntityAdapter**](###### Custom Adapter - Entity Adapter) \
-> constructor with parameters
```
public DownloadImageTask(String url, Handler uiHandler) {
	imageUrl = url;
	handler = uiHandler;
}
```
-> since it extends `Thread` it will have the method `run()`
```
@Override
public void run() {
    super.run();
    // we need a HTTP connection
    HttpURLConnection connection = null;
    // bitmap that we will send with the message and which will have the downloaded image
    Bitmap bitmap = null;
    try {
        URL url = new URL(imageUrl);
        // open connection
        connection = (HttpURLConnection) url.openConnection();
        // get data stream from connection
        InputStream inputStream = connection.getInputStream();
        // get bitmap
        bitmap = BitmapFactory.decodeStream(inputStream);

    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        connection.disconnect();
    }
    // generate message to send back
    Message message = new Message();
    // set code that we define as the "successful" code
    message.what = 1122;
    // set bitmap to obj
    message.obj = bitmap;
    // send at front of queue so message is received by EntityAdapter
    handler.sendMessageAtFrontOfQueue(message);
}
```