package io.itman.admin.vo;


import io.itman.model.SysMenubutton;

import java.util.List;

public class SysmenubuttonList {
    private List<SysMenubutton> sysmenubuttons;

    public List<SysMenubutton> getSysmenubuttons() {
        return sysmenubuttons;
    }

    public void setSysmenubuttons(List<SysMenubutton> sysmenubuttons) {
        this.sysmenubuttons = sysmenubuttons;
    }

    public SysmenubuttonList(){super();}

    public SysmenubuttonList(List<SysMenubutton> sysmenubuttons){

        super();

        this.sysmenubuttons=sysmenubuttons;
    }
}
