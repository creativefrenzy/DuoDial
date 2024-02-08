package com.privatepe.app.nGiftSec.listner;


import com.zeeplive.app.response.N_Gift.Gift;

import java.io.Serializable;

public interface MyTabListner extends Serializable {
    void onClick(int mainArrayPosition, int subArrayPosition, int adapterPosition, boolean isSelected, Gift selectedGifts);
}
