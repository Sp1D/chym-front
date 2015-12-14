/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sp1d.chym;

import net.sp1d.chym.entities.Torrent;

/**
 *
 * @author sp1d
 */
public interface FetchNSave {
    void go();
    void finish();
    void add(Torrent torrent);
}
