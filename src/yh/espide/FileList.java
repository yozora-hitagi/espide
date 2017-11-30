package yh.espide;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by yozora on 2017/11/28.
 */
public class FileList extends JScrollPane {


    private JList<Item> jlist;

    private DefaultListModel model;

    private ItemClickedListener itemclicked;

    public FileList() {
        model = new DefaultListModel();
        jlist = new JList();
        jlist.setModel(model);
        jlist.setCellRenderer(new Item());
        setViewportView(jlist);

        jlist.setOpaque(false);
        setOpaque(false);


        jlist.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = jlist.locationToIndex(e.getPoint());
                jlist.setSelectedIndex(index);
                Item item = jlist.getSelectedValue();
                if (e.getButton() == MouseEvent.BUTTON3) {
                    item.getComponentPopupMenu().show(e.getComponent(), e.getX(), e.getY());
                }

                if (null != itemclicked) {
                    itemclicked.ItemClicked(e, item);
                }


            }
        });

    }

    public int listsize() {
        return model.getSize();
    }


    public void setItemclicked(ItemClickedListener listener) {
        itemclicked = listener;
    }

    public void addItem(Item element) {
        model.addElement(element);
    }

    public void removeall() {
        model.removeAllElements();
    }

    public static interface ItemClickedListener {
        void ItemClicked(MouseEvent e, Item item);
    }

    public static class Item extends JLabel implements ListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Item item = (Item) value;

            item.setOpaque(false);

            item.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            item.setFont(Context.FONT_14);

            String text = item.getText();

            if (text.toUpperCase().endsWith(".LUA")) {
                item.setIcon(Icon.LUA);
            } else if (text.toUpperCase().endsWith(".LC")) {
                item.setIcon(Icon.LC);
            } else {
                item.setIcon(Icon.FILE);
            }

            Color foreground;

            // check if this cell represents the current DnD drop location
            JList.DropLocation dropLocation = list.getDropLocation();
            if (dropLocation != null
                    && !dropLocation.isInsert()
                    && dropLocation.getIndex() == index) {

                foreground = Color.WHITE;

                // check if this cell is selected
            } else if (isSelected) {
                foreground = Color.RED;

                // unselected, and not the DnD drop location
            } else {
                foreground = Color.BLACK;
            }


            item.setForeground(foreground);

            return (Item) value;

        }


    }


}
