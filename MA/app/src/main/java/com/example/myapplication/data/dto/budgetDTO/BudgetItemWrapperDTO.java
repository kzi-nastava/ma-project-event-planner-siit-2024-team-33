package com.example.myapplication.data.dto.budgetDTO;

import java.io.Serializable;
import java.util.List;

public class BudgetItemWrapperDTO implements Serializable {
    public Integer eventId;
    public BudgetItemDTO item;
    public List<BudgetOfferDTO> offers;
}
