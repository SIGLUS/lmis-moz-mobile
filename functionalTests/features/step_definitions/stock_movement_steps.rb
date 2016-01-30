require 'calabash-android/calabash_steps'
require 'pry'

Then(/^I select stock card code called "(.*?)"$/) do |name|
      q = query("android.widget.TextView id:'product_name' {text CONTAINS '#{name}'}")
      touch(q.last);
end

And(/^I select a reason "(.*?)" "(.*?)"$/) do |first_reason, second_reason|
    q = query("android.widget.TextView id:'tx_reason'")
    touch(q.last);
    steps %Q{
        Then I press "#{first_reason}"
        Then I press "#{second_reason}"
    	}
end

And(/^I clear banner message$/) do
    touch(query("android.widget.ImageView id:'iv_product_update_banner_clear'"))
end


And(/^I enter document number "(.*?)"$/) do |number|
    touch(query("android.widget.EditText id:'et_document_no'").last);
    keyboard_enter_text(number)
    hide_soft_keyboard
end

And(/^I enter received number "(.*?)"$/) do |number|
    touch(query("android.widget.EditText id:'et_received'").last);
    keyboard_enter_text(number)
    hide_soft_keyboard
end

And(/^I enter issued number "(.*?)"$/) do |number|
    q = query("android.widget.EditText id:'et_issued'")
    touch(q.last)
    keyboard_enter_text(number)
    hide_soft_keyboard
end

And(/^I enter negative adjustment number "(.*?)"$/) do |number|
    q = query("android.widget.EditText id:'et_negative_adjustment'")
    touch(q.last)
    keyboard_enter_text(number)
    hide_soft_keyboard
end

And(/^I enter positive adjustment number "(.*?)"$/) do |number|
    q = query("android.widget.EditText id:'et_positive_adjustment'")
    touch(q.last)
    keyboard_enter_text(number)
    hide_soft_keyboard
end

When(/^I search stockcard by code "(.*?)" and select this item$/) do |stock_card_code|
    search_bar = query("android.support.v7.widget.SearchView id:'action_search'")
    touch(search_bar)
    clear_text_in(search_bar)
    enter_text("android.support.v7.widget.SearchView id:'action_search'", stock_card_code)
    
    steps %Q{
        Then I select stock card code called "#{stock_card_code}"
    }
end

Then(/^I make a movement "(.*?)" "(.*?)" "(.*?)" "(.*?)" "(.*?)"$/) do |stock_card_code, first_reason, second_reason, movement_column, number|
    steps %Q{
        Then I search stockcard by code "#{stock_card_code}" and select this item
        Then I wait for "Stock Card" to appear
        Then I wait for 1 second
        And I select a reason "#{first_reason}" "#{second_reason}"
    }

    if movement_column.eql? "negative adjustment" or movement_column.eql? "positive adjustment" or movement_column.eql? "issued"
    # physical device could be too narrow for the negative adjustment column to show, so we need to swipe right for it too
        steps %Q{
            Then I swipe right
        }
    end
    steps %Q{
        Then I wait for 1 second
        And I enter #{movement_column} number "#{number}"
        Then I wait for "Complete" to appear
        And I press "Complete"
        And I sign with "superuser"
        Then I wait for 2 seconds
        Then I navigate back
        Then I wait for 1 second
        Then I navigate back
    }
end

Then(/^I see "(.*?)" in signature field$/) do |text|
    unless element_exists("android.widget.TextView id:'tx_signature' text:'#{text}'")
     		fail(msg="not found #{text} in signature field")
    end
end

And(/^I enter "(.*?)" into documentNo$/) do |documentNo|
    touch(query("android.widget.EditText id:'et_document_no'").last)
    keyboard_enter_text(documentNo)
    hide_soft_keyboard
end

Then(/^I make all movements for "(.*?)"$/) do |drugFNM|
    if EnvConfig::STRESS_TEST
        steps %Q{
          And I make a movement "#{drugFNM}" "Entries" "District( DDM)" "received" "5"
          And I make a movement "#{drugFNM}" "Entries" "Province ( DPM)" "received" "5"
          And I make a movement "#{drugFNM}" "Issues" "Public pharmacy" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "Maternity" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "General Ward" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "Accident & Emergency" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "Mobile unit" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "Laboratory" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "UATS" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "PNCTL" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "PAV" "issued" "2"
          And I make a movement "#{drugFNM}" "Issues" "Dental ward" "issued" "2"
          And I make a movement "#{drugFNM}" "Negative Adjustments" "Drugs in quarantine have expired, returned to Supplier" "negative adjustment" "2"
          And I make a movement "#{drugFNM}" "Negative Adjustments" "Damaged on arrival" "negative adjustment" "1"
          And I make a movement "#{drugFNM}" "Negative Adjustments" "Loans made from a health facility deposit" "negative adjustment" "1"
          And I make a movement "#{drugFNM}" "Negative Adjustments" "Inventory correction in case of over stock on Stock card (Stock on hand is less than stock in stock card)" "negative adjustment" "1"
          And I make a movement "#{drugFNM}" "Negative Adjustments" "Product defective, moved to quarantine" "negative adjustment" "1"
          And I make a movement "#{drugFNM}" "Positive Adjustments" "Returns from Customers(HF and dependent wards)" "positive adjustment" "1"
          And I make a movement "#{drugFNM}" "Positive Adjustments" "Returns of expired drugs (HF and dependent wards)" "positive adjustment" "1"
          And I make a movement "#{drugFNM}" "Positive Adjustments" "Donations to Deposit" "positive adjustment" "1"
          And I make a movement "#{drugFNM}" "Positive Adjustments" "Loans received at the health facility deposit" "positive adjustment" "1"
          And I make a movement "#{drugFNM}" "Positive Adjustments" "Inventory correction in case of under stock on Stock card (Stock on hand is more than stock in stock card)" "positive adjustment" "1"
          And I make a movement "#{drugFNM}" "Positive Adjustments" "Returns from Quarantine, in the case of quarantined product being fit for use" "positive adjustment" "1"
        }
    else
        steps %Q{
            And I make a movement "#{drugFNM}" "Entries" "District( DDM)" "received" "5"
            And I make a movement "#{drugFNM}" "Issues" "Public pharmacy" "issued" "2"
            And I make a movement "#{drugFNM}" "Negative Adjustments" "Drugs in quarantine have expired, returned to Supplier" "negative adjustment" "2"
            And I make a movement "#{drugFNM}" "Positive Adjustments" "Returns from Customers(HF and dependent wards)" "positive adjustment" "1"
        }
    end
end

Then(/^I should see CMM "(.*?)"$/) do |arg1|
 cmm = query("android.widget.TextView id:'tv_cmm'" , :text)
 unless cmm.at(0).to_i == arg1.to_i
     fail "Calculation of CMM is not correct"
 end
end

Then(/^I should see lowStock "(\d+)" and warning on product "(.*?)"$/) do |low_stock,product_code|
    current_soh = query("android.widget.TextView id:'tv_stock_on_hand'" , :text).first.to_i
    condition = (current_soh == low_stock.to_i)
    warning_icon = query("android.widget.ImageView id:'iv_warning'")

    unless condition
        fail "Calculation of low_stock is not correct"
    end

    unless !(warning_icon.empty?)
        fail "Low stock warning not appear"
    end
end

Then(/^I should see last document number "(\d+)"$/) do |documentNo|
    actual = query("android.widget.EditText id:'et_document_no'", :text)[-2]
    unless actual == documentNo
        fail "Document Number Does not perform correctly"
    end
end

Then(/^I shouldn't see lowStock "(\d+)" and warning on product "(.*?)"$/) do |low_stock,product_code|
    current_soh = query("android.widget.TextView id:'tv_stock_on_hand'" , :text).first.to_i
    condition = (current_soh == low_stock.to_i)
    warning_icon = query("android.widget.ImageView id:'iv_warning'")

    unless condition
        fail "Calculation of low_stock is not correct"
    end

    unless (warning_icon.empty?)
        fail "Low stock warning should not appear"
    end

end
