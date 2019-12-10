# MAS Project

## Selling protocol

Buyer sends its own name with `CFP`.

The seller responds with a string like this `name_of_buyer,selling_quantity,selling_price,seller_name`, this is why it's forbidden to put commas in agent's name.

This is a CSV format, do not put space.

To confirm transaction seller sends `name_of_buyer,selling_quantity,selling_price,seller_name` to avoid having to persist those informations in the seller side.