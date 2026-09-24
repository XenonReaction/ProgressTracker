-- Replace Hibernate's generated constraint names with ones that say what they are:
-- <table>_<column>_fk for foreign keys and <table>_<column>_unique for unique constraints.
-- The entities declare the same names.

alter table users rename constraint ukr43af9ap4edm43mmtq01oddj6 to users_username_unique;

alter table nodes rename constraint fkt67ih9ta1epitin6upf11sh27 to nodes_user_id_fk;
alter table node_links rename constraint fkitb245un9qusy9w2tsiemv6ts to node_links_node_id_fk;

alter table trees rename constraint fk492i19ei0gikgxgcj00bflhs1 to trees_user_id_fk;
alter table tree_tags rename constraint fk3t042ytiooba6r04w5i34hmpb to tree_tags_tree_id_fk;

alter table tree_nodes rename constraint fkhoi18dkkpcx3nadic3spfpkcj to tree_nodes_tree_id_fk;
alter table tree_nodes rename constraint fkqblynt4o8gnu7rr3vivcwv8rn to tree_nodes_node_id_fk;

alter table prerequisites rename constraint fkker9yasa0wtiukrw3awfp0oh2 to prerequisites_prerequisite_tree_node_id_fk;
alter table prerequisites rename constraint fkcfutkrxa5alu4s5nib4jg3eip to prerequisites_dependent_tree_node_id_fk;
