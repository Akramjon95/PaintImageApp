package maxcoders.uz.broadcastapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class BitmapActivity extends AppCompatActivity {
    private ImageView iv_FloodFillActivity_image,iv_FloodFillActivity_image2;
    private TextView generate, textSize;
    private Spinner spinner1 , spinner2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);

        iv_FloodFillActivity_image = findViewById(R.id.iv_FloodFillActivity_image);
        iv_FloodFillActivity_image2 = findViewById(R.id.iv_FloodFillActivity_image2);

        iv_FloodFillActivity_image.setVisibility(View.GONE);
        iv_FloodFillActivity_image2.setVisibility(View.GONE);

        generate = findViewById(R.id.generate);
        textSize = findViewById(R.id.textSize);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this,R.array.algorithm, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_FloodFillActivity_image.setVisibility(View.VISIBLE);
                iv_FloodFillActivity_image2.setVisibility(View.VISIBLE);
            }
        });

        iv_FloodFillActivity_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    floodColor(event.getX(), event.getY());
                }
                return true;
            }
        });

        iv_FloodFillActivity_image2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    floodColorSecond(event.getX(), event.getY());
                }
                return true;
            }
        });

        textSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showCustomDialog();
            }
        });
    }

    private void showCustomDialog(){

                AlertDialog.Builder dialog = new AlertDialog.Builder(BitmapActivity.this);

                LayoutInflater inflater = getLayoutInflater();
                View view1 = inflater.inflate(R.layout.custom_dialog, null);

                dialog.setView(view1)
                 .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialoginterface, int i) {
                      dialoginterface.dismiss();
                      }})
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                dialoginterface.dismiss();
                            }
                        }).show();
    }

    private void floodColor(float x, float y) {
        final Point p1 = new Point();
        p1.x = (int) x;// X and y are co - ordinates when user clicks on the screen
        p1.y = (int) y;

        Bitmap bitmap = getBitmapFromVectorDrawable(iv_FloodFillActivity_image.getDrawable());
        //bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int pixel = bitmap.getPixel((int) x, (int) y);
        int sourceColorRGB = Color.red(pixel);
//                Color.green(pixel),
//                Color.blue(pixel)
//        };

        final int targetColor = Color.parseColor("#FF2200");
//        int toleranceHex = Color.parseColor("#545454");
        QueueLinearFloodFiller queueLinearFloodFiller = new QueueLinearFloodFiller(bitmap, sourceColorRGB, targetColor);

        int toleranceHex = Color.parseColor("#545454");
        int[] toleranceRGB = new int[]{
                Color.red(toleranceHex),
                Color.green(toleranceHex),
                Color.blue(toleranceHex)
        };
        queueLinearFloodFiller.setTolerance(toleranceRGB);
        queueLinearFloodFiller.setFillColor(targetColor);
        queueLinearFloodFiller.setTargetColor(sourceColorRGB);
        queueLinearFloodFiller.floodFill(p1.x, p1.y);

        bitmap = queueLinearFloodFiller.getImage();
        iv_FloodFillActivity_image.setImageBitmap(bitmap);
    }

    private void floodColorSecond(float x, float y) {
        final Point p1 = new Point();
        p1.x = (int) x;// X and y are co - ordinates when user clicks on the screen
        p1.y = (int) y;

        Bitmap bitmap = getBitmapFromVectorDrawable(iv_FloodFillActivity_image2.getDrawable());
        //bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        int pixel = bitmap.getPixel((int) x, (int) y);
        int sourceColorRGB = Color.red(pixel);
//                Color.green(pixel),
//                Color.blue(pixel)
//        };

        final int targetColor = Color.parseColor("#FF2200");
//        int toleranceHex = Color.parseColor("#545454");
        QueueLinearFloodFiller queueLinearFloodFiller = new QueueLinearFloodFiller(bitmap, sourceColorRGB, targetColor);

        int toleranceHex = Color.parseColor("#545454");
        int[] toleranceRGB = new int[]{
                Color.red(toleranceHex),
                Color.green(toleranceHex),
                Color.blue(toleranceHex)
        };
        queueLinearFloodFiller.setTolerance(toleranceRGB);
        queueLinearFloodFiller.setFillColor(targetColor);
        queueLinearFloodFiller.setTargetColor(sourceColorRGB);
        queueLinearFloodFiller.floodFill(p1.x, p1.y);

        bitmap = queueLinearFloodFiller.getImage();
        iv_FloodFillActivity_image2.setImageBitmap(bitmap);
    }

    private Bitmap getBitmapFromVectorDrawable(Drawable drawable) {
        try {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}