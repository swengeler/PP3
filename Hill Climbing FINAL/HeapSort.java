/*
 * @(#)HeapSortAlgorithm.java   1.0 95/06/23 Jason Harrison
 *
 * Copyright (c) 1995 University of British Columbia
 *
 * Permission to use, copy, modify, and distribute this software
 * and its documentation for NON-COMMERCIAL purposes and without
 * fee is hereby granted provided that this copyright notice
 * appears in all copies. Please refer to the file "copyright.html"
 * for further important copyright and licensing information.
 *
 * UBC MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 * THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, OR NON-INFRINGEMENT. UBC SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

/**
 * A heap sort demonstration algorithm
 * SortAlgorithm.java, Thu Oct 27 10:32:35 1994
 *
 * Modified by Steven de Jong for Genetic Algorithms.
 * 
 * Modified by Jo Stevens for practical session.
 *
 *
 * @author Jason Harrison@cs.ubc.ca
 * @version     1.0, 23 Jun 1995
 *
 * @author Steven de Jong
 * @version     1.1, 08 Oct 2004
 * 
 * @author Jo Stevens
 * @version 1.2, 14 Nov 2008
 * 
 */
public class HeapSort 
{
    
    public static void sort(CargoSpace cargo[])
    {
        int N = cargo.length;
        
        for (int k = N/2; k > 0; k--) 
        downheap(cargo, k, N);

        do 
        {
            CargoSpace cargoSpace = cargo[0];
            cargo[0] = cargo[N - 1];
            cargo[N - 1] = cargoSpace;
            
            N = N - 1;
            downheap(cargo, 1, N);
        } 
        while (N > 1);
    }
    
    private static void downheap(CargoSpace cargo[], int k, int N)
    {
        CargoSpace cargoSpace = cargo[k - 1];
        
        while (k <= N/2) 
        {
            int j = k + k;
            if ((j < N) && (cargo[j - 1].getTotalValue() > cargo[j].getTotalValue()))
            j++;

            if (cargoSpace.getTotalValue() <= cargo[j - 1].getTotalValue()) 
            break;

            else 
            {
                cargo[k - 1] =cargo[j - 1];
                k = j;
            }
        }
        cargo[k - 1] = cargoSpace;
    }
}

