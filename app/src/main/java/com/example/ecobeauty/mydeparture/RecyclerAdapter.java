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

import com.google.android.material.snackbar.Snackbar;

import com.example.ecobeauty.R;


import java.util.ArrayList;
import java.util.List;

@SuppressLint("NewApi")
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemsViewHolder>{
	
	private List<Word> wordsList;
	Context mContext;
	static ClickListener clickListener;

	DBSQLiteHandler dbHandler;
	public ArrayList<Word> wordsListDB = new ArrayList<Word>();


	public RecyclerAdapter(Context con, List<Word> wordsList){
		this.wordsList=wordsList;
		this.mContext=con;
		this.dbHandler = new DBSQLiteHandler(mContext);
		this.wordsListDB = (ArrayList<Word>) wordsList;
	}
	
	@Override
	public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType){		
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
		return new ItemsViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ItemsViewHolder holder, int position) {

		Word wordMapper = wordsList.get(position);
		
		holder.txtViewIcon.setText(""+ wordMapper.getWord().charAt(0));
		holder.txtViewIcon.setGravity(Gravity.CENTER);
		holder.txtViewWord.setText(wordMapper.getWord());
		holder.txtViewPOS.setText(wordMapper.getPartOfSpeech());

		if(checkFavouriteItem(wordMapper)){

			Drawable starFilled = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_favourite_filled, null);
			starFilled.setBounds(0,0,24,24);
			holder.imgButtonFavourite.setBackground(starFilled);
			holder.imgButtonFavourite.setTag("filled");

		}else{
			
			Drawable starEmpty = ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_favourite,null);
			starEmpty.setBounds(0,0,24,24);
			holder.imgButtonFavourite.setBackground(starEmpty);
			holder.imgButtonFavourite.setTag("empty");

		}
	}
	
	@Override
	public int getItemCount() {
		int a =wordsList.size();
		return a;
	}

	public class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
		
		CardView cardView;
		TextView txtViewIcon,txtViewWord,txtViewPOS;
		ImageButton imgButtonFavourite;
		boolean starred = false;		
		
		public ItemsViewHolder(View itemView){
			super(itemView);
			
			cardView = (CardView) itemView.findViewById(R.id.cardViewID);
			txtViewIcon = (TextView) itemView.findViewById(R.id.txtView_iconEntry);
			txtViewWord = (TextView) itemView.findViewById(R.id.txtView_Word);
			txtViewPOS = (TextView) itemView.findViewById(R.id.txtView_PartOfSpeech);
			imgButtonFavourite = (ImageButton) itemView.findViewById(R.id.imgButton_Favourite);			
			
			itemView.setOnClickListener(this);	
			imgButtonFavourite.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					String tag = imgButtonFavourite.getTag().toString();
					if (tag.equalsIgnoreCase("empty") && !starred) {

						dbHandler.addWord(wordsList.get(getAdapterPosition()));

						imgButtonFavourite.setTag("filled");
						Drawable starFilled = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_favourite_filled, null);
						starFilled.setBounds(0, 0, 24, 24);
						imgButtonFavourite.setBackground(starFilled);

						Snackbar.make(view, R.string.goodJob, Snackbar.LENGTH_LONG).setAction(R.string.cancel,new View.OnClickListener() {
							
							@Override
							public void onClick(View view) {

								dbHandler.removeWord(wordsList.get(getAdapterPosition()));
								
								Drawable star = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_favourite, null);
								star.setBounds(0,0,24,24);
								imgButtonFavourite.setBackground(star);
							}
						}).show();
					} else {

						dbHandler.removeWord(wordsList.get(getAdapterPosition()));

						imgButtonFavourite.setTag("empty");
						Drawable starEmpty = ResourcesCompat.getDrawable(view.getResources(), R.drawable.ic_favourite, null);
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
		boolean check = false;

		ArrayList<Word> itemsInDB = dbHandler.getWords();
		
		if(itemsInDB!=null){
			for(Word word : itemsInDB){
				if((word.getWord()).equals(checkStarredItem.getWord())) {
					check = true;
					break;
				}
			}
		}
        return check;
	}	

	public void setListener(ClickListener clicked){

		RecyclerAdapter.clickListener = clicked;
	}	
	
	public interface ClickListener{
		void itemClicked(View view, int position);
	}
}
