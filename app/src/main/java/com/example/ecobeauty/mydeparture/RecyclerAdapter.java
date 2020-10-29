package com.example.ecobeauty.mydeparture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

@SuppressLint(Constants.NEW_API)
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsViewHolder>{
	
	private List<Word> wordsList;
	public ArrayList<Word> wordsListDB = new ArrayList<Word>(), itemsInDB;
	static ClickListener clickListener;
	private Context mContext;
	private DBSQLiteHandler dbHandler;
	private View view;
	private Word wordMapper;
	private Drawable starFilled, starEmpty, star;
	private int wordListSize;
	private CardView cardView;
	private TextView txtViewIcon,txtViewWord,txtViewPOS;
	private ImageButton imgButtonFavourite;
	private String tag;
	private boolean isChecked;

	public RecyclerAdapter(Context con, List<Word> wordsList){
		this.wordsList=wordsList;
		this.mContext=con;
		this.dbHandler = new DBSQLiteHandler(mContext);
		this.wordsListDB = (ArrayList<Word>) wordsList;
	}
	
	@Override
	public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){		
		view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
		return new ItemsViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ItemsViewHolder holder, int position) {
		wordMapper = wordsList.get(position);
		
		txtViewIcon.setText(""+ wordMapper.getWord().charAt(0));
		txtViewIcon.setGravity(Gravity.CENTER);
		txtViewWord.setText(wordMapper.getWord());
		txtViewPOS.setText(wordMapper.getPartOfSpeech());

		if(checkFavouriteItem(wordMapper)){
			starFilled = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_favourite_filled, null);
			starFilled.setBounds(0,0,24,24);
			imgButtonFavourite.setBackground(starFilled);
			imgButtonFavourite.setTag(Constants.FILLED);
			
		}else{
			starEmpty = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_favourite,null);
			starEmpty.setBounds(0,0,24,24);
			imgButtonFavourite.setBackground(starEmpty);
			imgButtonFavourite.setTag(Constants.EMPTY);
		}
	}
	
	@Override
	public int getItemCount() {
		wordListSize =wordsList.size();
		return wordListSize;
	}

	public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

		boolean starred = false;
		
		public ItemsViewHolder(View itemView){
			super(itemView);
			
			cardView = itemView.findViewById(R.id.cardViewID);
			txtViewIcon = itemView.findViewById(R.id.txtView_iconEntry);
			txtViewWord = itemView.findViewById(R.id.txtView_Word);
			txtViewPOS = itemView.findViewById(R.id.txtView_PartOfSpeech);
			imgButtonFavourite = itemView.findViewById(R.id.imgButton_Favourite);
			
			itemView.setOnClickListener(this);	
			imgButtonFavourite.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					tag = imgButtonFavourite.getTag().toString();
					if (tag.equalsIgnoreCase(Constants.EMPTY) && !starred) {

						dbHandler.addWord(wordsList.get(getAdapterPosition()));
						
						imgButtonFavourite.setTag(Constants.FILLED);
						starFilled = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_favourite_filled, null);
						starFilled.setBounds(0, 0, 24, 24);
						imgButtonFavourite.setBackground(starFilled);
						
						Snackbar.make(view, R.string.goodJob, Snackbar.LENGTH_LONG).setAction(R.string.cancel,new View.OnClickListener() {
							
							@Override
							public void onClick(View view) {
								dbHandler.removeWord(wordsList.get(getAdapterPosition()));

								star = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_favourite, null);
								star.setBounds(0,0,24,24);
								imgButtonFavourite.setBackground(star);
							}
						}).show();
					} else {
						dbHandler.removeWord(wordsList.get(getAdapterPosition()));
						imgButtonFavourite.setTag(Constants.EMPTY);
						starEmpty = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_favourite, null);
						starEmpty.setBounds(0, 0, 24, 24);
						imgButtonFavourite.setBackground(starEmpty);					
					}					
					starred =! starred;				
				}	
			});
		}
		
		@Override
		public void onClick(View view) {
			if(clickListener!=null){
				clickListener.itemClicked(view, getAdapterPosition());
			}
		}
	}
	
	
	public boolean checkFavouriteItem(Word checkStarredItem){
		isChecked = false;

		itemsInDB = dbHandler.getWords();
		
		if(itemsInDB!=null){
			for(Word word : itemsInDB){
				if((word.getWord()).equals(checkStarredItem.getWord())) {
					isChecked = true;
					break;
				}
			}
		}
        return isChecked;
	}	

	public void setListener(ClickListener clicked){
		RecyclerAdapter.clickListener = clicked;
	}	
	
	public interface ClickListener{
		void itemClicked(View view, int position);
	}
}
