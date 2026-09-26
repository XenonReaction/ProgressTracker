-- Phase 7.3: a node can list a flashcard deck as a resource. Decks belong to the Flashcards
-- module, so a deck resource stores the deck's id in target_id with no foreign key (modules
-- refer to each other's rows by plain id). The Flashcards module confirms the deck exists
-- when the resource is saved, and refuses to delete a deck that nodes list.

alter table node_resources add column target_id bigint;

alter table node_resources drop constraint node_resources_resource_type_check;
alter table node_resources add constraint node_resources_resource_type_check
    check (resource_type in ('url', 'tree', 'deck'));

alter table node_resources drop constraint node_resources_target_matches_type;
alter table node_resources add constraint node_resources_target_matches_type check (
    (resource_type = 'url' and url is not null and tree_id is null and target_id is null)
    or (resource_type = 'tree' and tree_id is not null and url is null and target_id is null)
    or (resource_type = 'deck' and target_id is not null and url is null and tree_id is null));

create index node_resources_target_id_idx on node_resources (target_id);
